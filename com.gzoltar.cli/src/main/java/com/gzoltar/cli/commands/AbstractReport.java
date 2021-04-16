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
import java.util.Locale;
import org.kohsuke.args4j.Option;
import com.gzoltar.cli.Command;
import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.instr.InstrumentationLevel;

/**
 * Base class for creating a report.
 */
public abstract class AbstractReport extends Command {

  @Option(name = "--buildLocation", usage = "location of Java class files", metaVar = "<path>",
      required = true)
  protected File buildLocation = null;

  @Option(name = "--outputDirectory", usage = "output directory for the report", metaVar = "<path>",
      required = true)
  protected File outputDirectory = null;

  @Option(name = "--dataFile", usage = "GZoltar *.ser file to process", metaVar = "<path>",
      required = true)
  protected File dataFile = null;

  @Option(name = "--includes",
      usage = "expression to identify which classes to report on, may use wildcard characters (* and ?) and ':' to define more than one expression",
      metaVar = "<expression(s)>", required = false)
  protected String includes = "*";

  @Option(name = "--excludes",
      usage = "expression to identify which classes to not report on, may use wildcard characters (* and ?) and ':' to define more than one expression",
      metaVar = "<expression(s)>", required = false)
  protected String excludes = "";

  protected final AgentConfigs agentConfigs;

  protected AbstractReport() {
    this.agentConfigs = new AgentConfigs();
    // reports do not require any bytecode injection
    this.agentConfigs.setInstrumentationLevel(InstrumentationLevel.NONE);
  }

  /**
   * 
   * @return
   */
  private boolean canGenerateReport(final PrintStream out, final PrintStream err) {
    if (this.buildLocation == null || !this.buildLocation.exists()) {
      err.println("Skipping GZoltar execution due to missing build location.");
      return false;
    }
    if (this.dataFile == null || !this.dataFile.exists()) {
      err.println("Skipping GZoltar execution due to missing execution data file.");
      return false;
    }
    if (this.outputDirectory == null) {
      err.println("Skipping GZoltar execution due to missing output directory.");
      return false;
    }
    return true;
  }

  /**
   * 
   * @param locale
   */
  protected abstract void generateReport(Locale locale) throws Exception;

  /**
   * {@inheritDoc}
   */
  @Override
  public int execute(final PrintStream out, final PrintStream err) throws Exception {
    out.println("* " + this.description());

    if (!this.canGenerateReport(out, err)) {
      return 1;
    }

    this.generateReport(Locale.getDefault());
    out.println("* Done!");

    return 0;
  }
}
