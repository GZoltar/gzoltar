package com.gzoltar.core.spectrum;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;
import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.Tree;
import com.gzoltar.core.model.Transaction;

/**
 * Serialization of a spectrum instance into binary streams.
 */
public class SpectrumWriter {

  /** File format version, will be incremented for each incompatible change. */
  public static final char FORMAT_VERSION;

  static {
    // Runtime initialize to ensure the compiler does not inline the value.
    FORMAT_VERSION = 0x0001;
  }

  /** Magic number in header for file format identification. */
  public static final char MAGIC_NUMBER = 0xC0C0;

  /** Block identifier for file headers. */
  public static final byte BLOCK_HEADER = 0x01;

  /** Block identifier for nodes information. */
  public static final byte BLOCK_NODE = 0x10;

  /** Block identifier for transaction information. */
  public static final byte BLOCK_TRANSACTION = 0x11;

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
    this.out.writeByte(BLOCK_HEADER);
    this.out.writeChar(MAGIC_NUMBER);
    this.out.writeChar(FORMAT_VERSION);
  }

  /**
   * Serializes a spectrum instance into binary streams.
   * 
   * @param spectrum
   * @throws IOException if the data can't be written
   */
  public void writeSpectrum(final ISpectrum spectrum) throws IOException {
    final Tree tree = spectrum.getTree();
    for (final Node node : tree.getNodes()) {
      this.writeNode(node);
    }
    for (final Transaction transaction : spectrum.getTransactions()) {
      this.writeTransaction(transaction);
    }
  }

  private void writeNode(final Node node) {
    try {
      this.out.writeByte(BLOCK_NODE);
      this.out.writeUTF(node.getName());
      if (node.isRoot()) {
        return;
      }

      this.out.writeInt(node.getLineNumber());
      this.out.writeUTF(node.getNodeType().name());
      this.out.writeUTF(node.getParent().getName());

      Map<String, Double> suspiciousnessValues = node.getSuspiciousnessValues();
      if (suspiciousnessValues != null) {
        this.out.writeInt(suspiciousnessValues.size());

        for (Entry<String, Double> suspiciousness : suspiciousnessValues.entrySet()) {
          this.out.writeUTF(suspiciousness.getKey());
          this.out.writeDouble(suspiciousness.getValue().doubleValue());
        }
      } else {
        this.out.writeInt(0); // there is not any suspiciousness value
      }
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void writeTransaction(final Transaction transaction) {
    if (transaction.hasActivations()) {
      try {
        this.out.writeByte(BLOCK_TRANSACTION);
        this.out.writeUTF(transaction.getName());
        this.out.writeInt(transaction.getNumberActivities());
        for (Node node : transaction.getActivity()) {
          this.out.writeUTF(node.getName());
        }
        this.out.writeBoolean(transaction.isError());
        this.out.writeInt(transaction.hashCode());
      } catch (final IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
