package com.gzoltar.core.spectrum;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;
import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.util.SerialisationIdentifiers;

/**
 * Serialization of a spectrum instance into binary streams.
 */
public class SpectrumWriter {

  private final DataOutputStream out;

  private final AgentConfigs agentConfigs;

  /**
   * Creates a new writer based on the given output stream. Depending on the nature of the
   * underlying stream output should be buffered as most data is written in single bytes.
   * 
   * @param output binary stream to write execution data to
   * @throws IOException if the header can't be written
   */
  public SpectrumWriter(final OutputStream output, final AgentConfigs agentConfigs)
      throws IOException {
    this.out = new DataOutputStream(output);
    this.agentConfigs = agentConfigs;
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

    // AgentConfigs
    this.out.writeByte(SerialisationIdentifiers.BLOCK_AGENT_CONFIGS);
    this.out.writeUTF(this.agentConfigs.getGranularity().name());
    this.out.writeBoolean(this.agentConfigs.getInclPublicMethods());
    this.out.writeBoolean(this.agentConfigs.getInclStaticConstructors());
    this.out.writeBoolean(this.agentConfigs.getInclDeprecatedMethods());
  }

  /**
   * Serializes a spectrum instance into binary streams.
   * 
   * @param spectrum
   * @throws IOException if the data can't be written
   */
  public void writeSpectrum(final ISpectrum spectrum) throws IOException {
    for (final Transaction transaction : spectrum.getTransactions()) {
      TransactionSerialize.serialize(this.out, spectrum, transaction);
    }
    this.out.close();
  }

  /**
   * 
   */
  private static final class TransactionSerialize {

    /**
     * Serialises an instance of {@link com.gzoltar.core.model.Transaction}.
     * 
     * @param out binary stream to write bytes to
     * @param spectrum
     * @param transaction
     * @throws IOException
     */
    public static void serialize(final DataOutputStream out, final ISpectrum spectrum,
        final Transaction transaction)
        throws IOException {
      if (transaction.hasActivations()) {
        out.writeByte(SerialisationIdentifiers.BLOCK_TRANSACTION);
        out.writeUTF(transaction.getName());

        Map<String, boolean[]> activity = transaction.getActivity();
        out.writeInt(activity.size());

        for (Entry<String, boolean[]> entry : activity.entrySet()) {
          out.writeUTF(entry.getKey()); // hash
          out.writeUTF(spectrum.getProbeGroupByHash(entry.getKey()).getName()); // name
          writeBooleanArray(out, entry.getValue()); // hitArray
        }

        out.writeUTF(transaction.getTransactionOutcome().name());
        out.writeLong(transaction.getRuntime());
        out.writeUTF(transaction.getStackTrace());
      }
    }

    /**
     * Writes a boolean array. Internally a sequence of boolean values is packed into single bits.
     * 
     * @param out
     * @param value boolean array
     * @throws IOException if thrown by the underlying stream
     */
    private static void writeBooleanArray(final DataOutputStream out, final boolean[] value)
        throws IOException {
      out.writeInt(value.length);
      int buffer = 0;
      int bufferSize = 0;
      for (final boolean b : value) {
        if (b) {
          buffer |= 0x01 << bufferSize;
        }
        if (++bufferSize == 8) {
          out.writeByte(buffer);
          buffer = 0;
          bufferSize = 0;
        }
      }
      if (bufferSize > 0) {
        out.writeByte(buffer);
      }
    }
  }

}
