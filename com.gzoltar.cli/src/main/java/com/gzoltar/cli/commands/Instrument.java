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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;
import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.instr.InstrumentationLevel;
import com.gzoltar.core.instr.Instrumenter;

/**
 * The <code>instrument</code> command.
 */
public class Instrument extends AbstractAgent implements Serializable {

  private static final long serialVersionUID = 6927579973183582263L;

  @Argument(usage = "list of folders or files to instrument recursively", metaVar = "<sourcefiles>",
      required = true)
  private List<File> sources = new ArrayList<File>();

  @Option(name = "--outputDirectory",
      usage = "path to which the instrumented classes will be written", metaVar = "<path>",
      required = true)
  private File outputDirectory;

  @Override
  public String description() {
    return "Off-line instrumentation of Java class files and jar files.";
  }

  @Override
  public int execute(PrintStream out, PrintStream err) throws Exception {
    out.println("* " + this.description());

    // create a brand new output directory
    if (this.outputDirectory.exists()) {
      FileUtils.deleteDirectory(this.outputDirectory);
    }
    this.outputDirectory.mkdirs();

    // configure instrumentation
    AgentConfigs agentConfigs = this.prepareAgentOptions();
    agentConfigs.setInstrumentationLevel(InstrumentationLevel.OFFLINE);
    Instrumenter instrumenter = new Instrumenter(agentConfigs);

    final File absoluteOut = this.outputDirectory.getAbsoluteFile();

    // instrument recursively
    int numInstrumentedClasses = 0;
    out.println("* Processing");
    for (File source : this.sources) {
      out.println("  - " + source.getAbsolutePath());
      if (source.isFile()) {
        numInstrumentedClasses += instrumenter.instrument(source, absoluteOut);
      } else {
        numInstrumentedClasses += instrumenter.instrumentRecursively(source, absoluteOut);
      }
    }

    out.println("* " + numInstrumentedClasses + " classes instrumented to " + absoluteOut);
    out.println("* Done!");

    return 0;
  }

}
