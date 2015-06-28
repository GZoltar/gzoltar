package pt.up.fe.aes.report;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import pt.up.fe.aes.base.spectrum.Spectrum;
import pt.up.fe.aes.report.metrics.AmbiguityMetric;
import pt.up.fe.aes.report.metrics.ApproximateEntropyMetric;
import pt.up.fe.aes.report.metrics.EntropyMetric;
import pt.up.fe.aes.report.metrics.Metric;
import pt.up.fe.aes.report.metrics.RhoMetric;
import pt.up.fe.aes.report.metrics.SimpsonMetric;

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
		
		List<Metric> metrics = new ArrayList<Metric>();
		Collections.addAll(metrics, new RhoMetric(), new SimpsonMetric(),
				new AmbiguityMetric(), new EntropyMetric(), new ApproximateEntropyMetric());
		
		for(Metric metric : metrics) {
			metric.setSpectrum(spectrum);
			System.out.println(metric.getName() + ": " + metric.calculate());
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
