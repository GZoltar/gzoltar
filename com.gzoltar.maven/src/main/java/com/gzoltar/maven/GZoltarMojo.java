package com.gzoltar.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "test")
@Execute(lifecycle = "gzoltar", phase = LifecyclePhase.TEST)
public class GZoltarMojo extends AbstractGZoltarMojo {

  public void executeMojo() throws MojoExecutionException, MojoFailureException {
    getLog().info("GZoltas has finished!");
  }

}
