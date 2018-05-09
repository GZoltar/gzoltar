package com.gzoltar.core.spectrum;

import static java.lang.String.format;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.util.SerialisationIdentifiers;

public class SpectrumReader {

  private final Spectrum spectrum;

  private final DataInputStream in;

  private boolean firstBlock = true;

  /**
   * Creates a new reader based on the given input stream input. Depending on the nature of the
   * underlying stream input should be buffered as most data is read in single bytes.
   * 
   * @param input input stream to read execution data from
   */
  public SpectrumReader(final InputStream input) {
    this.spectrum = new Spectrum();
    this.in = new DataInputStream(input);
  }

  public Spectrum getSpectrum() {
    return this.spectrum;
  }

  public boolean read() throws IOException {
    byte type;
    do {
      int i = this.in.read();
      if (i == -1) {
        return false; // EOF
      }
      type = (byte) i;
      if (this.firstBlock && type != SerialisationIdentifiers.BLOCK_HEADER) {
        throw new IOException("Invalid spectrum data file.");
      }
      this.firstBlock = false;
    } while (this.readBlock(type));
    return true;
  }

  private boolean readBlock(final byte blocktype) throws IOException {
    switch (blocktype) {
      case SerialisationIdentifiers.BLOCK_HEADER:
        this.readHeader();
        return true;
      case SerialisationIdentifiers.BLOCK_NODE:
        this.spectrum.addNode(Node.deserialize(this.in, this.spectrum.getTree()));
        return true;
      case SerialisationIdentifiers.BLOCK_TRANSACTION:
        this.spectrum.addTransaction(Transaction.deserialize(this.in, this.spectrum.getTree()));
        return true;
      default:
        throw new IOException(format("Unknown block type %x.", Byte.valueOf(blocktype)));
    }
  }

  private void readHeader() throws IOException {
    if (this.in.readChar() != SerialisationIdentifiers.MAGIC_NUMBER) {
      throw new IOException("Invalid execution data file.");
    }
    final char version = this.in.readChar();
    if (version != SerialisationIdentifiers.FORMAT_VERSION) {
      throw new IncompatibleSpectrumVersionException(version);
    }
  }

  /**
   * Signals that execution data in an incompatible version was tried to read.
   */
  public class IncompatibleSpectrumVersionException extends IOException {

    private static final long serialVersionUID = 1L;

    private final int actualVersion;

    /**
     * Creates a new exception to flag version mismatches in execution data.
     * 
     * @param actualVersion version found in the spectrum data
     */
    public IncompatibleSpectrumVersionException(final int actualVersion) {
      super(String.format(
          "Cannot read spectrum data version 0x%x. This version of GZoltar uses spectrum data version 0x%x.",
          Integer.valueOf(actualVersion), Integer.valueOf(SerialisationIdentifiers.FORMAT_VERSION)));
      this.actualVersion = actualVersion;
    }

    /**
     * Gets the version expected in the spectrum data which can be read by this version of GZoltar.
     * 
     * @return expected version in spectrum data
     */
    public int getExpectedVersion() {
      return SerialisationIdentifiers.FORMAT_VERSION;
    }

    /**
     * Gets the actual version found in the spectrum data.
     * 
     * @return actual version in spectrum data
     */
    public int getActualVersion() {
      return actualVersion;
    }

  }
}
