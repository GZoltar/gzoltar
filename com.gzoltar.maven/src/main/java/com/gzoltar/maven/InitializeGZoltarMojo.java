package com.gzoltar.maven;

import java.io.IOException;
import java.net.ServerSocket;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.messaging.Server;
import com.gzoltar.maven.messaging.ServiceHandlerFactory;

@Mojo(name = "init")
public class InitializeGZoltarMojo extends AbstractGZoltarMojo {

	static final String SUREFIRE_ARG_LINE = "argLine";
	static final String GZOLTAR_ARTIFACT = "com.gzoltar:com.gzoltar.core";

	public void executeGZoltarMojo() throws MojoExecutionException, MojoFailureException {
		
		try {
			AgentConfigs agentConfigs = new AgentConfigs();
			
			ServerSocket serverSocket = new ServerSocket(0);
			Server s = new Server(serverSocket, new ServiceHandlerFactory(this));
			s.start();

			getLog().info("Setting up instrumentation agent.");
			String agentFilename = this.getArtifact(GZOLTAR_ARTIFACT).getFile().getAbsolutePath();
			
			agentConfigs.setPort(serverSocket.getLocalPort());
			agentConfigs.setGranularityLevel(granularityLevel);
			agentConfigs.setPrefixesToFilter(prefixesToFilter);
			agentConfigs.setFilterTargetLocation(restrictOutputDirectory);
			agentConfigs.setFilterModifier(restrictToPublicMethods);
			
			setAgent(agentFilename, agentConfigs);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void setAgent(String agentFilename, AgentConfigs agentConfigs) {
		StringBuilder sb = new StringBuilder();

		sb.append(getProjectProperty(SUREFIRE_ARG_LINE))
			.append(" -D")
			.append(AgentConfigs.BUILD_LOCATION_KEY)
			.append("='")
			.append(project.getBuild().getDirectory())
			.append("' ")
			.append(argLine)
			.append(" -javaagent:\"")
			.append(agentFilename)
			.append("\"=")
			.append(agentConfigs.serialize());

		this.setProjectProperty(SUREFIRE_ARG_LINE, sb.toString());
	}

}
