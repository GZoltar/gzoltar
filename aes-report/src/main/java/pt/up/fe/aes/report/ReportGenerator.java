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
	private static final String INDEX_FILE = "visualization.html";
	private static final String METRICS_FILE = "metrics.txt";
	private static final String SEARCH_TOKEN = "window.data_ex={";

	private final String projectName;
	private final Spectrum spectrum;
	private List<Metric> metrics;

	public ReportGenerator(String projectName, Spectrum spectrum) {
		this.projectName = projectName;
		this.spectrum = spectrum;
	}


	public List<Metric> getMetrics() {
		if(metrics == null) {
			metrics = new ArrayList<Metric>();
			Collections.addAll(metrics, new RhoMetric(), new SimpsonMetric(),
					new AmbiguityMetric(), new EntropyMetric(), new ApproximateEntropyMetric());

			for(Metric metric : metrics) {
				metric.setSpectrum(spectrum);
			}
		}
		return metrics;
	}

	public void generate(File reportDirectory, List<String> classesToInstrument) {

		VisualizationData vd = new VisualizationData(spectrum);
		//System.out.println(vd.serialize());

		try {
			writeMetrics(reportDirectory, classesToInstrument);
			writeVisualization(reportDirectory, vd.serialize());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private void writeMetrics(File reportDirectory, List<String> classesToInstrument) throws IOException {
		File metricsFile = new File(reportDirectory, METRICS_FILE);
		
		//TODO: add class name, or name of project
		List<String> scores = new ArrayList<String>();
		for(Metric metric : getMetrics()) {
			scores.add(metric.getName() + ": " + metric.calculate());
		}
		
		FileUtils.writeLines(metricsFile, scores, false);
	}


	private void writeVisualization(File targetDir, String reportLine) throws IOException {

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
	}

}
