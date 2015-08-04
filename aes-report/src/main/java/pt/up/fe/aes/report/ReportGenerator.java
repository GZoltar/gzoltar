package pt.up.fe.aes.report;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import pt.up.fe.aes.base.spectrum.Spectrum;

public class ReportGenerator {

	private static final String DATA_FILE = "report-data.zip";
	private static final String INDEX_FILE = "visualization.html";
	private static final String METRICS_FILE = "metrics.txt";
	private static final String CLASS_METRICS_FILE = "class-metrics.txt";
	private static final String PACKAGE_METRICS_FILE = "package-metrics.txt";
	private static final String SEARCH_TOKEN = "window.data_ex={";

	private final OverallReport report;

	public ReportGenerator(String projectName, Spectrum spectrum, String granularity, List<String> classesToInstrument) {
		this.report = new OverallReport(projectName, spectrum, granularity, classesToInstrument);
	}

	public List<String> generate(File reportDirectory) {

		List<String> result = null; 
		VisualizationData vd = new VisualizationData(report.getSpectrum());

		try {
			result = writeMetrics(reportDirectory);
			writeVisualization(reportDirectory, vd);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}


	private List<String> writeMetrics(File reportDirectory) throws IOException {
		File metricsFile = new File(reportDirectory, METRICS_FILE);

		List<String> scores = report.getReport();
		FileUtils.writeLines(metricsFile, scores, false);

		//per-package metrics
		writeFilteredReport(reportDirectory, PACKAGE_METRICS_FILE, report.getPerPackageReports());

		//per-class metrics
		writeFilteredReport(reportDirectory, CLASS_METRICS_FILE, report.getPerClassReports());

		return scores;
	}


	private static void writeFilteredReport(File reportDirectory, String metricsFilename, 
			List<AbstractReport> reports) throws IOException {
		File metricsFile = new File(reportDirectory, metricsFilename);
		List<String> scores = new ArrayList<String>();
		for(AbstractReport r : reports) {
			scores.addAll(r.getReport());
			scores.add("");
		}
		FileUtils.writeLines(metricsFile, scores, false);
	}

	private void writeVisualization(File targetDir, VisualizationData vd) throws IOException {

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
			sb.insert(i + SEARCH_TOKEN.length(), vd.serialize());
			indexData = sb.toString();
		}

		File reportIndexDestination = new File(targetDir, INDEX_FILE);
		FileUtils.write(reportIndexDestination, indexData);
	}

}
