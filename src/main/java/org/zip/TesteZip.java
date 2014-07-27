package org.zip;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class TesteZip {
	public static void main(String[] args) throws IOException {
		testeLeituraZipComZipFile();
		testeLeituraZipComZipInputStream();
		
		UnZipper uz = new UnZipper();
		
		uz.extrair("c://zip/teste.zip", "c://zip/testeDeExtracao");
	}

	private static void testeLeituraZipComZipInputStream() throws IOException {
		System.out.println("Teste de leitura zip com ZipInputStream");
		
		String arquivo = "c://zip/teste.zip";
		
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
		
		String arquivo = "c://zip/teste.zip";
		
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
