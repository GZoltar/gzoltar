package com.gzoltar.cli.commands;

import java.io.File;
import java.io.PrintStream;
import java.io.Serializable;
import org.kohsuke.args4j.Option;
import com.gzoltar.cli.Command;
import com.gzoltar.cli.slave.Slave;
import com.gzoltar.cli.slave.SlaveListUnitTests;

/**
 * The <code>listTests</code> command.
 */
public class ListUnitTests extends Command implements Serializable {

  private static final long serialVersionUID = -7315345017175317773L;

  @Option(name = "--projectCP", usage = "classpath of the project under test",
      metaVar = "<directories and/or jar files separated by ':'>", required = true)
  private String projectCP;

  @Option(name = "--outputDirectory", usage = "path to which the 'outputFile' will be written",
      metaVar = "<path>", required = true)
  private File outputDirectory;

  @Option(name = "--outputFile",
      usage = "file to which the name of all (JUnit/TestNG) unit test cases in the classpath will be written (default 'outputDirectory/tests')",
      metaVar = "<file>", required = false)
  private String outputFile = "tests";

  @Option(name = "--includes",
      usage = "list of classes allowed to be loaded, may use wildcard characters (* and ?)",
      metaVar = "<classes>", required = false)
  private String includes = "*";

  @Option(name = "--excludes",
      usage = "list of classes not allowed to be loaded, may use wildcard characters (* and ?)",
      metaVar = "<classes>", required = false)
  private String excludes = "";

  /**
   * {@inheritDoc}
   */
  @Override
  public String description() {
    return "List all (JUnit/TestNG) unit test cases in a provided classpath.";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String name() {
    return "listTests";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int execute(final PrintStream out, final PrintStream err) throws Exception {
    out.println("* " + this.description());
    Slave.launch(out, err, this, SlaveListUnitTests.class.getCanonicalName(), this.projectCP,
        this.timeout);
    return 0;
  }

  /**
   * 
   * @return
   */
  public File getOutputDirectory() {
    return this.outputDirectory;
  }

  /**
   * 
   * @return
   */
  public String getOutputFile() {
    return this.outputFile == null ? "tests" : this.outputFile;
  }

  /**
   * 
   * @return
   */
  public String getIncludes() {
    return this.includes;
  }

  /**
   * 
   * @return
   */
  public String getExcludes() {
    return this.excludes;
  }
}
