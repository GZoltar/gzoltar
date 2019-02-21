/**
 * Copyright (C) 2018 GZoltar contributors.
 * 
 * This file is part of GZoltar.
 * 
 * GZoltar is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * GZoltar is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with GZoltar. If
 * not, see <https://www.gnu.org/licenses/>.
 */
package com.gzoltar.cli.commands;

import java.io.File;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;
import com.gzoltar.cli.Command;
import com.gzoltar.cli.test.FindTestMethods;
import com.gzoltar.cli.test.TestMethod;
import com.gzoltar.cli.utils.SystemProperties;
import com.gzoltar.core.instr.actions.BlackList;
import com.gzoltar.core.instr.actions.WhiteList;
import com.gzoltar.core.instr.filter.Filter;
import com.gzoltar.core.instr.matchers.ClassNameMatcher;
import com.gzoltar.core.instr.matchers.JUnitMatcher;
import com.gzoltar.core.instr.matchers.TestNGMatcher;

/**
 * The <code>listTestMethods</code> command.
 */
public class ListTestMethods extends Command {

  @Argument(usage = "list of folders that contain test classes", metaVar = "<path>",
      required = true)
  private List<File> testDirs = new ArrayList<File>();

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
    return "listTestMethods";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int execute(final PrintStream out, final PrintStream err) throws Exception {
    out.println("* " + this.description());

    // only test classes
    WhiteList includeJUnitClasses = new WhiteList(new JUnitMatcher());
    WhiteList includeTestNGClasses = new WhiteList(new TestNGMatcher());
    // load some test classes
    WhiteList includeClasses = new WhiteList(new ClassNameMatcher(this.includes));
    // do not load some test classes
    BlackList excludeClasses = new BlackList(new ClassNameMatcher(this.excludes));

    Filter filter =
        new Filter(includeJUnitClasses, includeTestNGClasses, includeClasses, excludeClasses);

    PrintWriter testsWriter = new PrintWriter(
        this.outputDirectory + SystemProperties.FILE_SEPARATOR + this.outputFile, "UTF-8");

    for (File testDir : this.testDirs) {
      for (TestMethod testMethod : FindTestMethods.findTestMethodsInPath(filter, testDir)) {
        testsWriter.println(testMethod.getLongName());
      }
    }

    testsWriter.close();

    return 0;
  }
}
