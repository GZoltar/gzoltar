package com.gzoltar.core.spectrum;

import static java.lang.String.format;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.instr.InstrumentationLevel;
import com.gzoltar.core.instr.Instrumenter;
import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.model.TransactionOutcome;
import com.gzoltar.core.runtime.Collector;
import com.gzoltar.core.util.SerialisationIdentifiers;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public class SpectrumReader {

  private final Spectrum spectrum;

  private final DataInputStream in;

  private boolean firstBlock = true;

  private Instrumenter instrumenter = null;

  private final TransactionDeserialize transactionDeserialize = new TransactionDeserialize();

  /**
   * Creates a new reader based on the given input stream input. Depending on the nature of the
   * underlying stream input should be buffered as most data is read in single bytes.
   * 
   * @param input input stream to read execution data from
   */
  public SpectrumReader(final String buildLocation, final InputStream input) {
    this.spectrum = Collector.instance().getSpectrum();
    this.in = new DataInputStream(input);

    try {
      ClassPool.getDefault().appendClassPath(buildLocation);
    } catch (NotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public Spectrum getSpectrum() {
    return this.spectrum;
  }

  public boolean read() throws Exception {
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
    this.in.close();
    return true;
  }

  private boolean readBlock(final byte blocktype) throws Exception {
    switch (blocktype) {
      case SerialisationIdentifiers.BLOCK_HEADER:
        this.readHeader();
        return true;
      case SerialisationIdentifiers.BLOCK_AGENT_CONFIGS:
        final AgentConfigs agentConfigs = new AgentConfigs();
        agentConfigs.setGranularity(this.in.readUTF());
        agentConfigs.setInclPublicMethods(this.in.readBoolean());
        agentConfigs.setInclStaticConstructors(this.in.readBoolean());
        agentConfigs.setInclDeprecatedMethods(this.in.readBoolean());
        // disable any bytecode injection
        agentConfigs.setInstrumentationLevel(InstrumentationLevel.NONE);
        this.instrumenter = new Instrumenter(agentConfigs);
        return true;
      case SerialisationIdentifiers.BLOCK_TRANSACTION:
        this.spectrum.addTransaction(this.transactionDeserialize.deserialize());
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
   * 
   */
  private final class TransactionDeserialize {

    /**
     * Deserialises and create an instance of {@link com.gzoltar.core.model.Transaction}.
     * 
     * @return a {@link com.gzoltar.core.model.Transaction} object
     * @throws IOException
     */
    public Transaction deserialize() throws IOException, CloneNotSupportedException {
      String transactionName = in.readUTF();

      Map<String, boolean[]> activity = new LinkedHashMap<String, boolean[]>();
      int numberActivities = in.readInt();
      while (numberActivities > 0) {
        String probeGroupHash = in.readUTF();
        String probeGroupName = in.readUTF();
        boolean[] hitArray = this.readBooleanArray();

        // instrument probeGroup (in case it has been not been instrumented)
        if (spectrum.getProbeGroupByHash(probeGroupHash) == null) {
          // probeGroup has not been instrumented
          try {
            CtClass ctClass = ClassPool.getDefault().get(probeGroupName);
            instrumenter.instrument(ctClass);
          } catch (Exception e) {
            throw new RuntimeException(e);
          }

          // sanity check
          if (spectrum.getProbeGroupByHash(probeGroupHash) == null) {
            throw new RuntimeException("ProbeGroup '" + probeGroupHash + "' | '" + probeGroupName
                + "' has not been added to the spectrum instance!");
          }
        }

        activity.put(probeGroupHash, hitArray);
        numberActivities--;
      }

      TransactionOutcome transactionOutcome = TransactionOutcome.valueOf(in.readUTF());
      long runtime = in.readLong();
      String stackTrace = in.readUTF();

      return new Transaction(transactionName, activity, transactionOutcome, runtime, stackTrace);
    }

    /**
     * Reads a boolean array.
     * 
     * @return boolean array
     * @throws IOException if thrown by the underlying stream
     */
    private boolean[] readBooleanArray() throws IOException {
      final boolean[] value = new boolean[in.readInt()];
      int buffer = 0;
      for (int i = 0; i < value.length; i++) {
        if ((i % 8) == 0) {
          buffer = in.readByte();
        }
        value[i] = (buffer & 0x01) != 0;
        buffer >>>= 1;
      }
      return value;
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
          Integer.valueOf(actualVersion),
          Integer.valueOf(SerialisationIdentifiers.FORMAT_VERSION)));
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
