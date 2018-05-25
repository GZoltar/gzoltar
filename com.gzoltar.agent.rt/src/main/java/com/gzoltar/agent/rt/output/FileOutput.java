package com.gzoltar.agent.rt.output;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import com.gzoltar.core.AgentConfigs;
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

  public void writeSpectrum(final ISpectrum spectrum) throws Exception {
    final OutputStream output = this.openFile();
    try {
      final SpectrumWriter writer = new SpectrumWriter(output);
      writer.writeSpectrum(spectrum);
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
