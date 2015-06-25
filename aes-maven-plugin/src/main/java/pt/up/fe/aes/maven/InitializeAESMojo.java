package pt.up.fe.aes.maven;

import java.io.IOException;
import java.net.ServerSocket;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

import pt.up.fe.aes.base.messaging.Server;
import pt.up.fe.aes.maven.messaging.ServiceHandlerFactory;

@Mojo(name = "init")
public class InitializeAESMojo extends AbstractAESMojo {

	static final String SUREFIRE_ARG_LINE = "argLine";
	static final String AES_ARTIFACT = "pt.up.fe.aes:aes-base";

	public void executeAESMojo() throws MojoExecutionException, MojoFailureException {

		try {
			ServerSocket serverSocket = new ServerSocket(0);
			Server s = new Server(serverSocket, new ServiceHandlerFactory(this));
			s.start();

			getLog().info("Setting up instrumentation agent.");
			String agentFilename = this.getArtifact(AES_ARTIFACT).getFile().getAbsolutePath();
			setAgent(agentFilename, serverSocket.getLocalPort());

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void setAgent(String agentFilename, int port) {
		StringBuilder sb = new StringBuilder();

		sb.append(getProjectProperty(SUREFIRE_ARG_LINE))
			.append(" ")
			.append(argLine)
			.append(" -javaagent:\"")
			.append(agentFilename)
			.append("\"=")
			.append(port);

		this.setProjectProperty(SUREFIRE_ARG_LINE, sb.toString());
	}

}
