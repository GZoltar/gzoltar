package pt.up.fe.aes.report;

import java.io.File;
import java.io.IOException;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import pt.up.fe.aes.base.spectrum.Spectrum;

public class ReportGenerator {

	private static final String DATA_FILE = "report-data.zip";
	private static final String INDEX_FILE = "index.html";
	private static final String SEARCH_TOKEN = "window.data_ex={";
		
	public static void generate(File reportDirectory, Spectrum spectrum) {
		
		System.out.println("Reporting " + reportDirectory);
		spectrum.print();
		VisualizationData vd = new VisualizationData(spectrum);
		System.out.println(vd.serialize());
		
		try {
			writeReport(reportDirectory, vd.serialize());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private static File writeReport(File targetDir, String reportLine) throws IOException {
		
		ClassLoader classLoader = ReportGenerator.class.getClassLoader();
				
		File temp = File.createTempFile("aes-temp-file", ".zip"); 
		temp.deleteOnExit();
		FileUtils.copyInputStreamToFile(classLoader.getResourceAsStream(DATA_FILE), temp);
		try {
			ZipFile zipFile = new ZipFile(temp);
			 zipFile.extractAll(targetDir.getAbsolutePath());
		} catch (ZipException e) {
			e.printStackTrace();
		}
		
		String indexData = IOUtils.toString(classLoader.getResourceAsStream(INDEX_FILE));
		int i = indexData.indexOf(SEARCH_TOKEN);
		if (i != -1) {
			StringBuilder sb = new StringBuilder(indexData);
			sb.insert(i + SEARCH_TOKEN.length(), reportLine);
			indexData = sb.toString();
		}
		
		File reportIndexDestination = new File(targetDir, INDEX_FILE);
		FileUtils.write(reportIndexDestination, indexData);
		
		return reportIndexDestination;
	}

}
