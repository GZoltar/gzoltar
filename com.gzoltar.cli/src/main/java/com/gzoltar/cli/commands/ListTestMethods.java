/**
 * Copyright (C) 2020 GZoltar contributors.
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
import org.jacoco.core.runtime.WildcardMatcher;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;
import com.gzoltar.cli.Command;
import com.gzoltar.core.test.FindTestMethods;
import com.gzoltar.core.test.TestMethod;

/**
 * The <code>listTestMethods</code> command.
 */
public class ListTestMethods extends Command {

  @Argument(usage = "list of folders that contain test classes", metaVar = "<path>",
      required = true)
  private List<File> testClassesDirs = new ArrayList<File>();

  @Option(name = "--outputFile",
      usage = "file to which the name of all (JUnit/TestNG) unit test cases in the classpath will be written (default 'tests.txt')",
      metaVar = "<file>", required = false)
  private String outputFile = "tests.txt";

  @Option(name = "--includes",
      usage = "expression to identify which test methods to consider, may use wildcard characters (* and ?) and ':' to define more than one expression",
      metaVar = "<expression(s)>", required = false)
  private String includes = "*";

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

    PrintWriter testsWriter = new PrintWriter(this.outputFile, "UTF-8");

    for (File testClassesDir : this.testClassesDirs) {
      for (TestMethod testMethod : FindTestMethods.findTestMethodsInPath(testClassesDir,
          new WildcardMatcher(this.includes))) {
        testsWriter.println(testMethod.getClassType().name() + "," + testMethod.getLongName());
      }
    }

    testsWriter.close();

    return 0;
  }
}
