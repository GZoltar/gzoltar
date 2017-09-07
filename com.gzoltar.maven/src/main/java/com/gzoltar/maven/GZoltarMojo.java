package com.gzoltar.maven;

import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import com.gzoltar.core.model.Node;
import com.gzoltar.core.spectrum.FilteredSpectrumBuilder;
import com.gzoltar.core.spectrum.Spectrum;
import com.gzoltar.report.ReportGenerator;

@Mojo(name = "test")
@Execute(lifecycle = "gzoltar", phase = LifecyclePhase.TEST)
public class GZoltarMojo extends AbstractGZoltarMojo {

	public void executeGZoltarMojo() throws MojoExecutionException, MojoFailureException {

		Spectrum spectrum = retrieveCurrentSpectrum();
		
		if (spectrum == null || spectrum.getTransactionsSize() == 0) {
			throw new MojoFailureException("Could not gather coverage information. Exiting GZoltar analysis.");
		}
		
		if (classesToInstrument != null && !classesToInstrument.isEmpty()) {
			
			FilteredSpectrumBuilder fsb = new FilteredSpectrumBuilder().setSource(spectrum);
			
			for(String _class : classesToInstrument) {
				Node n = spectrum.getTree().findNode(_class);
				fsb.includeNode(n);
			}
			
			spectrum = fsb.getSpectrum();
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
