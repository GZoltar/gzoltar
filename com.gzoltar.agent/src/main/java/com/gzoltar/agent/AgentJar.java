package com.gzoltar.agent;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * API to access the agent JAR file as a resource.
 */
public final class AgentJar {

  /**
   * Name of the agent JAR file resource within this bundle.
   */
  private static final String RESOURCE = "/gzoltaragent.jar";

  private static final String ERRORMSG =
      String.format("The resource %s has not been found. Please see "
          + "/org.gzoltar.agent/README.TXT for more information.", RESOURCE);

  private AgentJar() {
    // no-op
  }

  /**
   * Returns the content of the JAR file as a stream.
   * 
   * @return content of the JAR file
   */
  public static InputStream getResourceAsStream() {
    final InputStream stream = AgentJar.class.getResourceAsStream(RESOURCE);
    if (stream == null) {
      throw new AssertionError(ERRORMSG);
    }
    return stream;
  }

  /**
   * Extract the GZoltar agent JAR and put it into a temporary location. This file should be deleted
   * on exit, but may not if the VM is terminated
   * 
   * @return Location of the Agent Jar file in the local file system. The file should exist and be
   *         readable.
   * @throws IOException Unable to unpack the agent jar
   */
  public static File extractToTempLocation() throws IOException {
    final File agentJar = File.createTempFile("gzoltaragent", ".jar");
    agentJar.deleteOnExit();

    extractTo(agentJar);

    return agentJar;
  }

  /**
   * Extract the GZoltar agent JAR and put it into the specified location.
   * 
   * @param destination Location to write GZoltar Agent Jar to. Must be writable
   * @throws IOException Unable to unpack the agent jar
   */
  private static void extractTo(File destination) throws IOException {
    InputStream inputJarStream = getResourceAsStream();
    OutputStream outputJarStream = null;

    try {
      outputJarStream = new FileOutputStream(destination);

      final byte[] buffer = new byte[8192];

      int bytesRead;
      while ((bytesRead = inputJarStream.read(buffer)) != -1) {
        outputJarStream.write(buffer, 0, bytesRead);
      }
    } finally {
      safeClose(inputJarStream);
      safeClose(outputJarStream);
    }
  }

  /**
   * Close a stream ignoring any error
   * 
   * @param closeable stream to be closed
   */
  private static void safeClose(Closeable closeable) {
    try {
      if (closeable != null) {
        closeable.close();
      }
    } catch (IOException e) {
      // no-op
    }
  }
}
