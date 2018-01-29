package com.gzoltar.core.spectrum;

import static java.lang.String.format;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.Set;
import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.NodeType;
import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.model.TransactionOutcome;
import com.gzoltar.core.model.Tree;

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
      if (this.firstBlock && type != SpectrumWriter.BLOCK_HEADER) {
        throw new IOException("Invalid spectrum data file.");
      }
      this.firstBlock = false;
    } while (this.readBlock(type));
    return true;
  }

  private boolean readBlock(final byte blocktype) throws IOException {
    switch (blocktype) {
      case SpectrumWriter.BLOCK_HEADER:
        this.readHeader();
        return true;
      case SpectrumWriter.BLOCK_NODE:
        this.readNode();
        return true;
      case SpectrumWriter.BLOCK_TRANSACTION:
        this.readTransaction();
        return true;
      default:
        throw new IOException(format("Unknown block type %x.", Byte.valueOf(blocktype)));
    }
  }

  private void readHeader() throws IOException {
    if (this.in.readChar() != SpectrumWriter.MAGIC_NUMBER) {
      throw new IOException("Invalid execution data file.");
    }
    final char version = this.in.readChar();
    if (version != SpectrumWriter.FORMAT_VERSION) {
      throw new IncompatibleSpectrumVersionException(version);
    }
  }

  private void readNode() throws IOException {
    String name = this.in.readUTF();
    if (Tree.ROOT_NAME.equals(name)) {
      return;
    }

    Integer lineNumber = this.in.readInt();
    String symbol = this.in.readUTF();
    String parentName = this.in.readUTF();

    Node parent = this.spectrum.getTree().getNode(parentName);
    assert parent != null;

    Node node = new Node(name, lineNumber, NodeType.valueOf(symbol), parent);

    int numberOfSuspiciousnessValues = this.in.readInt();
    while (numberOfSuspiciousnessValues > 0) {
      node.addSuspiciousnessValue(this.in.readUTF(), this.in.readDouble());

      numberOfSuspiciousnessValues--;
    }

    this.spectrum.addNode(node);
  }

  private void readTransaction() throws IOException {
    String name = this.in.readUTF();

    Set<Node> activity = new LinkedHashSet<Node>();
    int numberActivities = this.in.readInt();
    while (numberActivities > 0) {
      Node node = this.spectrum.getTree().getNode(this.in.readUTF());
      assert node != null;
      activity.add(node);

      numberActivities--;
    }

    TransactionOutcome transactionOutcome = TransactionOutcome.valueOf(this.in.readUTF());
    long runtime = this.in.readLong();
    String stackTrace = this.in.readUTF();
    int hashCode = this.in.readInt();

    Transaction transaction =
        new Transaction(name, activity, hashCode, transactionOutcome, runtime, stackTrace);
    this.spectrum.addTransaction(transaction);
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
          Integer.valueOf(actualVersion), Integer.valueOf(SpectrumWriter.FORMAT_VERSION)));
      this.actualVersion = actualVersion;
    }

    /**
     * Gets the version expected in the spectrum data which can be read by this version of GZoltar.
     * 
     * @return expected version in spectrum data
     */
    public int getExpectedVersion() {
      return SpectrumWriter.FORMAT_VERSION;
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
