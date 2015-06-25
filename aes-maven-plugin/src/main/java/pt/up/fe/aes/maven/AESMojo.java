package pt.up.fe.aes.maven;

import java.io.File;

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
		getLog().info("Executing AES Mojo.");

		Spectrum spectrum = retrieveCurrentSpectrum();
		//spectrum.print();

		
		ReportGenerator.generate(reportDirectory, spectrum);
	}

}
