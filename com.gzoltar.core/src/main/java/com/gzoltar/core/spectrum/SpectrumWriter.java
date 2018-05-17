package com.gzoltar.core.spectrum;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.model.Tree;
import com.gzoltar.core.util.SerialisationIdentifiers;

/**
 * Serialization of a spectrum instance into binary streams.
 */
public class SpectrumWriter {

  private final DataOutputStream out;

  /**
   * Creates a new writer based on the given output stream. Depending on the nature of the
   * underlying stream output should be buffered as most data is written in single bytes.
   * 
   * @param output binary stream to write execution data to
   * @throws IOException if the header can't be written
   */
  public SpectrumWriter(final OutputStream output) throws IOException {
    this.out = new DataOutputStream(output);
    this.writeHeader();
  }

  /**
   * Writes an file header to identify the stream and its protocol version.
   * 
   * @throws IOException if the header can't be written
   */
  private void writeHeader() throws IOException {
    this.out.writeByte(SerialisationIdentifiers.BLOCK_HEADER);
    this.out.writeChar(SerialisationIdentifiers.MAGIC_NUMBER);
    this.out.writeChar(SerialisationIdentifiers.FORMAT_VERSION);
  }

  /**
   * Serializes a spectrum instance into binary streams.
   * 
   * @param spectrum
   * @throws IOException if the data can't be written
   */
  public void writeSpectrum(final ISpectrum spectrum) throws IOException {
    final Tree tree = spectrum.getTree();
    for (final Node node : tree.getTargetNodes()) {
      if (node.hasBeenExecuted()) {
        node.serialize(this.out);
      }
    }
    for (final Transaction transaction : spectrum.getTransactions()) {
      transaction.serialize(this.out);
    }
    this.out.close();
  }
}
