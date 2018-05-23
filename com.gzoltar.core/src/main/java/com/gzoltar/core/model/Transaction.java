package com.gzoltar.core.model;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import com.gzoltar.core.runtime.ProbeGroup;

public class Transaction {

  private final String name;

  private final Map<String, ProbeGroup> probeGroups;

  private final TransactionOutcome outcome;

  private final long runtime;

  private final String stackTrace;

  /**
   * 
   * @param name
   * @param activity
   * @param isError
   */
  public Transaction(final String name, final Map<String, ProbeGroup> probeGroups,
      final TransactionOutcome outcome, final long runtime, final String stackTrace) {
    this.name = name;
    this.probeGroups = probeGroups;
    this.outcome = outcome;
    this.runtime = runtime;
    this.stackTrace = this.getNormalizedStackTrace(stackTrace);
  }

  /**
   * Returns the name of a transaction.
   */
  public String getName() {
    return this.name;
  }

  // === ProbeGroups ===

  /**
   * Returns true if a transaction has any activity, false otherwise.
   */
  public boolean hasActivations() {
    return !this.probeGroups.isEmpty();
  }

  /**
   * Returns the activities of a transaction.
   */
  public Map<String, Pair<String, boolean[]>> getActivity() {
    // <hash, <name, hitArray>>
    Map<String, Pair<String, boolean[]>> activity =
        new LinkedHashMap<String, Pair<String, boolean[]>>();

    for (ProbeGroup probeGroup : this.probeGroups.values()) {
      assert probeGroup.hasHitArray() == true;
      activity.put(probeGroup.getHash(),
          new ImmutablePair<String, boolean[]>(probeGroup.getName(), probeGroup.getHitArray()));
    }

    return activity;
  }

  /**
   * Returns all executed {@link com.gzoltar.core.model.Node} objects.
   * @return
   */
  public List<Node> getHitNodes() {
    List<Node> hitNodes = new ArrayList<Node>();
    for (ProbeGroup probeGroup : this.probeGroups.values()) {
      assert probeGroup.hasHitArray() == true;
      hitNodes.addAll(probeGroup.getHitNodes());
    }
    return hitNodes;
  }

  /**
   * Returns the number of activities.
   */
  public int getNumberHitNodes() {
    return this.getHitNodes().size();
  }

  /**
   * Returns true if a specific node of a probeGroup has been executed, false otherwise.
   */
  public boolean isNodeActived(String probeGroupHash, int nodeIndex) {
    if (!this.probeGroups.containsKey(probeGroupHash)) {
      return false;
    }
    return this.probeGroups.get(probeGroupHash).hitNode(nodeIndex);
  }

  // === Outcome ===

  /**
   * Returns the outcome of a transaction.
   */
  public TransactionOutcome getTransactionOutcome() {
    return this.outcome;
  }

  /**
   * Returns true if a transaction has failed, false otherwise.
   */
  public boolean hasFailed() {
    return this.outcome.equals(TransactionOutcome.FAIL);
  }

  // === Runtime ===

  /**
   * Returns the runtime of a transaction.
   */
  public long getRuntime() {
    return this.runtime;
  }

  // === StackTrace ===

  /**
   * Returns the stack trace of a failing transaction.
   */
  public String getStackTrace() {
    return this.stackTrace;
  }

  /**
   * Converts a multi-line formatted stack trace in one line. It also truncates the stack trace to a
   * certain amount of bytes (i.e., unsigned short).
   * 
   * @param stackStrace A multi-line formatted stack trace
   * @return
   */
  private String getNormalizedStackTrace(final String stackStrace) {
    if (stackStrace == null || stackStrace.isEmpty()) {
      return "";
    }

    String normalizedString =
        // kill newlines and surrounding space
        stackStrace.replaceAll(" *\r?\n[ \t]*", " ")
            // strip whitespace
            .replaceAll("^[ \t\r\n]*|[ \t\r\n]*$", "");

    // the stack trace of a, for example, java.lang.StackOverflowError could be extremely long, in
    // such cases the stack trace is truncated to a certain amount of bytes to keep it short

    Charset utf8 = Charset.forName(StandardCharsets.UTF_8.name());
    int maxNumBytes = Short.MAX_VALUE * 2; // unsigned short

    byte[] normalizedStringBytes = normalizedString.getBytes(utf8);
    if (normalizedStringBytes.length <= maxNumBytes) {
      return normalizedString;
    }

    // ensure truncation by having a byte buffer = maxNumBytes
    ByteBuffer byteBuffer = ByteBuffer.wrap(normalizedStringBytes, 0, maxNumBytes);
    CharBuffer charBuffer = CharBuffer.allocate(normalizedString.length());

    CharsetDecoder decoder = utf8.newDecoder();
    decoder.onMalformedInput(CodingErrorAction.IGNORE); // ignore any incomplete character
    decoder.decode(byteBuffer, charBuffer, true);
    decoder.flush(charBuffer);

    return new String(charBuffer.array(), 0, charBuffer.position());
  }

  // === Overrides ===

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append(this.name);
    sb.append("\t");
    for (ProbeGroup probeGroup : this.probeGroups.values()) {
      assert probeGroup.hasHitArray() == true;

      boolean[] arr = probeGroup.getHitArray();
      for (int i = 0; i < arr.length; i++) {
        if (arr[i]) {
          sb.append("1 ");
        } else {
          sb.append("0 ");
        }
      }
    }

    if (this.hasFailed()) {
      sb.append(TransactionOutcome.FAIL.getSymbol());
    } else {
      sb.append(TransactionOutcome.PASS.getSymbol());
    }

    sb.append(" hashcode: ");
    sb.append(this.hashCode());

    return sb.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(this.name);
    builder.append(this.probeGroups);
    builder.append(this.outcome);
    builder.append(this.stackTrace);
    return builder.toHashCode();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Transaction)) {
      return false;
    }

    Transaction transaction = (Transaction) obj;

    EqualsBuilder builder = new EqualsBuilder();
    builder.append(this.name, transaction.name);
    builder.append(this.probeGroups, transaction.probeGroups);
    builder.append(this.outcome, transaction.outcome);
    builder.append(this.stackTrace, transaction.stackTrace);

    return builder.isEquals();
  }

}
