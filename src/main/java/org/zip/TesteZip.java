package org.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class TesteZip {
	private static final String PASTA_EXTRACAO = "f://zip-teste/testeDeExtracao";
	private static final String PASTA_COMPACTAR = "f://Testes JAVA/";
	private static final String ARQUIVO_ZIP_LEITURA = "f://zip-teste/teste.zip";
	private static final String ARQUIVO_ZIP_ESCRITA = "f://zip-teste/meu_compactado.zip";

	public static void main(String[] args) throws IOException {
		testeLeituraZipComZipFile();
		testeLeituraZipComZipInputStream();
		
		UnZipper uz = new UnZipper();
		
		System.out.println(uz.extrair(ARQUIVO_ZIP_LEITURA, PASTA_EXTRACAO));
		
		Zipper z = new Zipper();
		
		File zipSaida = new File(ARQUIVO_ZIP_ESCRITA);
		File conteudoASerCompactado = new File(PASTA_COMPACTAR);
		
		z.criarZip(zipSaida, new File[]{conteudoASerCompactado});
		z.criarZip(new FileOutputStream("f://zip-teste/blabla.zip"), new File("t://PFIles"), new File("t://New Folder"));
	}

	private static void testeLeituraZipComZipInputStream() throws IOException {
		System.out.println("Teste de leitura zip com ZipInputStream");
		
		String arquivo = ARQUIVO_ZIP_LEITURA;
		
		FileInputStream fis = new FileInputStream(arquivo);
		ZipInputStream zis = new ZipInputStream(fis);
		ZipEntry entrada = null;
		while((entrada = zis.getNextEntry()) != null) {
			System.out.println(entrada.getName());
		}
		
		zis.close();
		fis.close();
		
		System.out.println("-----------------------------------------------------");
	}

	private static void testeLeituraZipComZipFile() throws IOException {
		System.out.println("Teste de leitura zip com ZipFile");
		
		String arquivo = ARQUIVO_ZIP_LEITURA;
		
		ZipFile zip = new ZipFile(arquivo);		
		Enumeration<? extends ZipEntry> e = zip.entries();
		
		while(e.hasMoreElements()){
			ZipEntry arquivoCompactado = e.nextElement();
			System.out.println(arquivoCompactado.getName());
		}
		
		zip.close();
		
		System.out.println("-----------------------------------------------------");
	}
}
