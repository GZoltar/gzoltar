package pt.up.fe.aes.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import pt.up.fe.aes.base.spectrum.Spectrum;
import pt.up.fe.aes.report.ReportGenerator;
import pt.up.fe.aes.report.metrics.Metric;

@Mojo(name = "test")
@Execute(lifecycle = "aes", phase = LifecyclePhase.TEST)
public class AESMojo extends AbstractAESMojo {

	public void executeAESMojo() throws MojoExecutionException, MojoFailureException {
		getLog().info("");
		getLog().info("Metric scores for project " + project.getName() + ":");

		Spectrum spectrum = retrieveCurrentSpectrum();

		ReportGenerator reportGenerator = new ReportGenerator(spectrum);
		
		for (Metric metric : reportGenerator.getMetrics()) {
			getLog().info(metric.getName() + ": " + metric.calculate());
		}
		getLog().info("");
		
		getLog().info("Writing report at " + reportDirectory.getAbsolutePath() + ".");
		reportGenerator.generate(reportDirectory);
	}

}
