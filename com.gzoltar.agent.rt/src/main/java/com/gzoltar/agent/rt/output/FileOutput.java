package com.gzoltar.agent.rt.output;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.core.spectrum.SpectrumReader;
import com.gzoltar.core.spectrum.SpectrumWriter;

public class FileOutput implements IAgentOutput {

  private File destFile;

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

    // read it back
    SpectrumReader s = new SpectrumReader(new FileInputStream(this.destFile));
    s.read();
  }

  private OutputStream openFile() throws IOException {
    final FileOutputStream file = new FileOutputStream(this.destFile, false);
    // Avoid concurrent writes from different agents running in parallel:
    file.getChannel().lock();
    return file;
  }
}
