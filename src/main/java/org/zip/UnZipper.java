package org.zip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class UnZipper {
	private static final int TAMANHO_BUFFER = 4024;
	
	/**
	 * Extrai o arquivo zip informado para a pasta informada.
	 * @param arquivoZip arquivo zip a ser extraido.
	 * @param pasta String que representa a pasta para o arquivo ser� extraido.
	 */
	public void extrair(File arquivoZip, String pasta) {
		extrair(arquivoZip, new File(pasta));
	}
	
	/**
	 * Extrai o arquivo zip informado para a pasta informada.
	 * @param arquivoZip String que representa o arquivo (com caminho completo) a ser extraido.
	 * @param pasta Local onde o arquivo zip ser� extraido.
	 */
	public void extrair(String arquivoZip, File pasta) {
		extrair(new File(arquivoZip), pasta);
	}
	
	/**
	 * Extrai o arquivo zip informado para a pasta informada.
	 * @param arquivoZip String que representa o arquivo (com caminho completo) a ser extraido.
	 * @param pasta String que representa o local (com caminho completo) onde o arquivo ser� extraido.
	 */
	public void extrair(String arquivoZip, String pasta) {
		extrair(new File(arquivoZip), new File(pasta));
	}
	
	/**
	 * Extrai o arquivo zip informado para a pasta informada;
	 * @param arquivoZip arquivo zip que ser� extraido.
	 * @param pasta local onde o arquivo ser� extra�do. Caso a pasta n�o exista, ela ser� criada.
	 * 
	 * @throws IllegalArgumentException se pasta n�o for uma pasta.
	 */
	public void extrair(File arquivoZip, File pasta) {
		if(!pasta.exists()) {
			pasta.mkdir();
		}
		
		if(!pasta.isDirectory()) {
			throw new IllegalArgumentException("A pasta informada para extra��o n�o � v�lida");
		}
		
		ZipFile zip = null;
		File arquivo = null;
		InputStream is = null;
		OutputStream os = null;
		byte[] buffer = new byte[TAMANHO_BUFFER];
		
		try {
			zip = new ZipFile(arquivoZip);
			Enumeration<? extends ZipEntry> entradas = zip.entries();
			
			while(entradas.hasMoreElements()) {
				ZipEntry entradaAtual = entradas.nextElement();
				
				arquivo = new File(pasta, entradaAtual.getName());
				
				if(entradaAtual.isDirectory() && !arquivo.exists()) {
					arquivo.mkdirs();
				}
				
				if(!arquivo.getParentFile().exists()) {
					arquivo.getParentFile().mkdirs();
				}
				
				
				try {
					is = zip.getInputStream(entradaAtual);
					os = new FileOutputStream(arquivo);
					
					int bytesLidos = 0;
					
					if(is == null) {
						throw new ZipException("Erro ao ler entrada do zip: " + entradaAtual.getName());
					}
					
					while((bytesLidos = is.read(buffer)) > 0) {
						os.write(buffer, 0, bytesLidos);
					}
					
				} finally {
					if(is != null) {
						try {
							is.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					if(os != null) {
						try {
							os.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (ZipException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(zip != null) {
				try {
					zip.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}