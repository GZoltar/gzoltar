package pt.up.fe.aes.report;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import pt.up.fe.aes.base.spectrum.Spectrum;

public class ReportGenerator {

	public static void main(String... args) {
		//Get file from resources folder
		ClassLoader classLoader = ReportGenerator.class.getClassLoader();
		File file = new File(classLoader.getResource("report-data").getFile());
		
		try {
			FileUtils.copyDirectory(file, new File("/Users/aperez/Desktop/tmp/report-data/"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(file);
	}

	
	public static void generate(String destFolder, Spectrum spectrum) {
		System.out.println("Reporting " + destFolder);
		spectrum.print();
	}

}
