package pt.up.fe.aes.maven;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import pt.up.fe.aes.base.instrumentation.granularity.GranularityFactory;
import pt.up.fe.aes.base.spectrum.Spectrum;

public abstract class AbstractAESMojo extends AbstractMojo {

	private static final String SPECTRUM_KEY = "CurrentSpectrum";
	
	@Parameter(property = "plugin.artifactMap")
	private Map<String, Artifact> pluginArtifactMap;
	
	@Parameter(property = "project")
	protected MavenProject project;
	
	@Parameter(defaultValue = " ")
	protected String argLine;
	
	@Parameter(defaultValue = "method")
	protected GranularityFactory.GranularityLevel granularityLevel;
	
	@Parameter
	protected List<String> classesToInstrument;
	
	@Parameter
	protected List<String> prefixesToFilter;
	
	@Parameter(defaultValue = "${project.build.directory}/aes-report/")
	protected File reportDirectory;
	
	@Parameter(defaultValue = "true")
	protected boolean restrictOutputDirectory;
	
	protected boolean shouldInstrument() {
		return project != null && !"pom".equals(project.getPackaging());
	}
	
	protected Artifact getArtifact(String name) {
		return pluginArtifactMap.get(name);
	}

	protected String getProjectProperty(String key) {
		return project.getProperties().getProperty(key, "");
	}
	
	protected void setProjectProperty(String key, String value) {
		project.getProperties().setProperty(key, value);
	}
	
	@SuppressWarnings("unchecked")
	public void storeCurrentSpectrum(Spectrum spectrum) {
		getPluginContext().put(SPECTRUM_KEY, spectrum);
	}
	
	public Spectrum retrieveCurrentSpectrum() {
		Object obj = getPluginContext().get(SPECTRUM_KEY);
		if(obj instanceof Spectrum) {
			return (Spectrum) obj;
		}
		return null;
	}
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		if(this.shouldInstrument()) {
			executeAESMojo();
		}
	}

	protected abstract void executeAESMojo() throws MojoExecutionException, MojoFailureException;
}
