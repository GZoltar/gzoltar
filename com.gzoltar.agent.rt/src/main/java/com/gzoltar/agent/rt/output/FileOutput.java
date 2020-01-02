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
package com.gzoltar.agent.rt.output;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.core.spectrum.SpectrumWriter;

public class FileOutput implements IAgentOutput {

  private final File destFile;

  public FileOutput(final AgentConfigs agentConfigs) {
    this.destFile = new File(agentConfigs.getDestfile()).getAbsoluteFile();
    final File folder = this.destFile.getParentFile();
    if (folder != null) {
      folder.mkdirs();
    }
    // Make sure we can write to the file:
    try {
      this.openFile().close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeSpectrum(final ISpectrum spectrum) throws Exception {
    final OutputStream output = this.openFile();
    try {
      final SpectrumWriter writer = new SpectrumWriter(output);
      writer.writeSpectrum(spectrum);
    } finally {
      output.close();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeTransaction(final Transaction transaction) throws IOException {
    final OutputStream output = this.openFile();
    try {
      final SpectrumWriter writer = new SpectrumWriter(output);
      writer.writeTransaction(transaction);
    } finally {
      output.close();
    }
  }

  private OutputStream openFile() throws IOException {
    final FileOutputStream file = new FileOutputStream(this.destFile, true);
    // Avoid concurrent writes from different agents running in parallel:
    file.getChannel().lock();
    return file;
  }
}
