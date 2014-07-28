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
	private static final int TAMANHO_BUFFER = 2048;
	
	/**
	 * Extrai o arquivo zip informado para a pasta informada.
	 * @param arquivoZip arquivo zip a ser extraido.
	 * @param pasta String que representa a pasta para o arquivo será extraido.
	 * 
	 * @return <code>true</code> se a não houve erros na extração ou <code>false</code> caso contrário.
	 * 
	 * @throws IllegalArgumentException se pasta não for uma pasta válida.
	 */
	public Boolean extrair(File arquivoZip, String pasta) {
		return extrair(arquivoZip, new File(pasta));
	}
	
	/**
	 * Extrai o arquivo zip informado para a pasta informada.
	 * @param arquivoZip String que representa o arquivo (com caminho completo) a ser extraido.
	 * @param pasta Local onde o arquivo zip será extraido.
	 * 
	 * @return <code>true</code> se a não houve erros na extração ou <code>false</code> caso contrário.
	 * 
	 * @throws IllegalArgumentException se pasta não for uma pasta válida.
	 */
	public Boolean extrair(String arquivoZip, File pasta) {
		return extrair(new File(arquivoZip), pasta);
	}
	
	/**
	 * Extrai o arquivo zip informado para a pasta informada.
	 * @param arquivoZip String que representa o arquivo (com caminho completo) a ser extraido.
	 * @param pasta String que representa o local (com caminho completo) onde o arquivo será extraido.
	 * 
	 * @return <code>true</code> se a não houve erros na extração ou <code>false</code> caso contrário.
	 * 
	 * @throws IllegalArgumentException se pasta não for uma pasta válida.
	 */
	public Boolean extrair(String arquivoZip, String pasta) {
		return extrair(new File(arquivoZip), new File(pasta));
	}
	
	/**
	 * Extrai o arquivo zip informado para a pasta informada;
	 * @param arquivoZip arquivo zip que será extraido.
	 * @param pasta local onde o arquivo será extraído. Caso a pasta não exista, ela será criada.
	 * 
	 * @return <code>true</code> se a não houve erros na extração ou <code>false</code> caso contrário.
	 * 
	 * @throws IllegalArgumentException se pasta não for uma pasta válida.
	 */
	public Boolean extrair(File arquivoZip, File pasta) {
		Boolean conseguiuExtrair = true;
		
		ZipFile zip = null;
		File arquivo = null;
		InputStream is = null;
		OutputStream os = null;
		byte[] buffer = new byte[TAMANHO_BUFFER];
		
		
		try {
			if(!pasta.exists()) {
				pasta.mkdirs();
			}
			
			if(!pasta.isDirectory() || !pasta.exists()) {
				throw new IllegalArgumentException("A pasta informada para extração não é válida");
			}
			
			zip = new ZipFile(arquivoZip);
			Enumeration<? extends ZipEntry> entradas = zip.entries();
			
			while(entradas.hasMoreElements()) {
				ZipEntry entradaAtual = entradas.nextElement();
				
				arquivo = new File(pasta, entradaAtual.getName());
				
				if(entradaAtual.isDirectory() && !arquivo.exists()) {
					arquivo.mkdirs();
					continue;
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
			System.out.println(e.getMessage());
			conseguiuExtrair = false;
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
			conseguiuExtrair = false;
			
		} finally {
			if(zip != null) {
				try {
					zip.close();
				} catch (IOException e) {
					System.out.println("Erro ao fechar arquivo zip");;
				}
			}
		}
		
		return conseguiuExtrair;
	}
}