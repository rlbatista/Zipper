package org.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zipper {
	private static final int TAMANHO_BUFFER = 2048;
	
	public void criarZip(File arquivoZip, File... arquivos) throws IOException {
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		
		try{
			if(!arquivoZip.getName().toLowerCase().endsWith(".zip")) {
				arquivoZip = new File(arquivoZip.getAbsolutePath() + ".zip");
			}
			
			fos = new FileOutputStream(arquivoZip);
			bos = new BufferedOutputStream(fos, TAMANHO_BUFFER);
			criarZip(bos, arquivos);
			
		} finally {
			if(bos != null) {
				try{
					bos.close();
				} catch(Exception e) {
					System.out.println(e.getMessage());
				}
			}
			
			if(fos != null) {
				try{
					fos.close();
				} catch(Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}

	public void criarZip(OutputStream osZip, File... arquivos) throws IOException {
		if(arquivos == null || arquivos.length < 1) {
			throw new IllegalArgumentException("Adicione ao menos um arquivo ou diretórios para compatação");
		}
		
		ZipOutputStream zos = null;
		
		try {
			zos = new ZipOutputStream(osZip);
			
			for (File arquivoAtual : arquivos) {
				String caminhoInicial = arquivoAtual.getParent();
				
				adicionaArquivoNoZip(zos, arquivoAtual, caminhoInicial);
			}
			
		} finally {
			if(zos != null) {
				zos.close();
			}
		}
	}
	
	private void adicionaArquivoNoZip(ZipOutputStream zos, File arquivo, String caminhoInicial) throws IOException {
		if(arquivo.isDirectory()) {
			File[] arquivos = arquivo.listFiles();
			for (File arquivoAtual : arquivos) {
				adicionaArquivoNoZip(zos, arquivoAtual, caminhoInicial);
			}
			
			return;
		}
		
		String caminhoEntradaZip = "";
		
		int idxCaminhoInicial = arquivo.getAbsolutePath().indexOf(caminhoInicial);
		if(idxCaminhoInicial >= 0) {
			caminhoEntradaZip = arquivo.getAbsolutePath().substring(idxCaminhoInicial + caminhoInicial.length());
		}
		
		ZipEntry entrada = new ZipEntry(caminhoEntradaZip);
		
		zos.putNextEntry(entrada);
		zos.setMethod(ZipOutputStream.DEFLATED);
		
		FileInputStream fis = new FileInputStream(arquivo);
		BufferedInputStream bis = new BufferedInputStream(fis);
		
		int bytesLidos = 0;
		byte[] buffer = new byte[TAMANHO_BUFFER];
		while((bytesLidos = bis.read(buffer, 0, TAMANHO_BUFFER)) != -1) {
			zos.write(buffer, 0, bytesLidos);
		}
		
		bis.close();
		fis.close();
	}
}
