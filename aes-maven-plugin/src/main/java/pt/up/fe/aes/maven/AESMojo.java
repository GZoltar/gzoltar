package pt.up.fe.aes.maven;

import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import pt.up.fe.aes.base.spectrum.Spectrum;
import pt.up.fe.aes.report.ReportGenerator;

@Mojo(name = "test")
@Execute(lifecycle = "aes", phase = LifecyclePhase.TEST)
public class AESMojo extends AbstractAESMojo {

	public void executeAESMojo() throws MojoExecutionException, MojoFailureException {

		Spectrum spectrum = retrieveCurrentSpectrum();
		
		if (spectrum == null || spectrum.getTransactionsSize() == 0) {
			throw new MojoFailureException("Could not gather coverage information. Exiting AES analysis.");
		}
		
		ReportGenerator reportGenerator = new ReportGenerator(project.getName(), 
															  spectrum, 
															  granularityLevel.name(),
															  classesToInstrument);		
		List<String> metrics = reportGenerator.generate(reportDirectory);
		
		getLog().info("");
		for(String metricDescription : metrics) {
			getLog().info(metricDescription);
		}
		getLog().info("");
		getLog().info("Writing report at " + reportDirectory.getAbsolutePath() + ".");
	}

}
