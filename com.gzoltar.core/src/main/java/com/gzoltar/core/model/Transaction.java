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
package com.gzoltar.core.model;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.tuple.Pair;
import com.gzoltar.core.runtime.ProbeGroup;

public class Transaction {

  private final String name;

  /** <ProbeGroup hash, hitArray> */
  private final Map<String, Pair<String, boolean[]>> activity;

  private final TransactionOutcome outcome;

  private final long runtime;

  private final String stackTrace;

  /**
   * 
   * @param name
   * @param outcome
   * @param runtime
   * @param stackTrace
   */
  public Transaction(final String name, final TransactionOutcome outcome, final long runtime,
      final String stackTrace) {
    this(name, new LinkedHashMap<String, Pair<String, boolean[]>>(), outcome, runtime, stackTrace);
  }

  /**
   * 
   * @param name
   * @param activity
   * @param outcome
   * @param runtime
   * @param stackTrace
   */
  public Transaction(final String name, final Map<String, Pair<String, boolean[]>> activity,
      final TransactionOutcome outcome, final long runtime, final String stackTrace) {
    this.name = name;
    this.activity = activity;
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
   * Returns all probeGroups hash.
   */
  public Set<String> getProbeGroupsHash() {
    return this.activity.keySet();
  }

  /**
   * Returns true if a transaction has any activity, false otherwise.
   */
  public boolean hasActivations() {
    return !this.activity.isEmpty();
  }

  /**
   * Returns the activities of a transaction.
   */
  public Map<String, Pair<String, boolean[]>> getActivity() {
    return this.activity;
  }

  /**
   * Adds an activity to a transaction.
   */
  public void addActivity(final String hash, final Pair<String, boolean[]> hitArray) {
    this.activity.put(hash, hitArray);
  }

  /**
   * Returns a boolean hit array of a probeGroup.
   */
  public boolean[] getHitArray(final ProbeGroup probeGroup) {
    return this.activity.get(probeGroup.getHash()).getRight();
  }

  /**
   * Returns a boolean hit array of a probeGroup.
   */
  public boolean[] getHitArrayByProbeGroupHash(final String hash) {
    return this.activity.get(hash).getRight();
  }

  /**
   * Returns true if a specific probe of a probeGroup has been executed, false otherwise.
   */
  public boolean isProbeActived(final ProbeGroup probeGroup, final int probeIndex) {
    if (!this.activity.containsKey(probeGroup.getHash())) {
      return false;
    }
    return this.activity.get(probeGroup.getHash()).getRight()[probeIndex];
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

    sb.append("[ProbeGroup] ");
    sb.append(this.name);
    sb.append("\n");
    sb.append("  Executed ");
    sb.append(this.activity.size());
    sb.append(" probeGroups");
    sb.append("\n");
    sb.append("  Pass/Fail ");
    if (this.hasFailed()) {
      sb.append(TransactionOutcome.FAIL.getSymbol());
    } else {
      sb.append(TransactionOutcome.PASS.getSymbol());
    }
    sb.append("\n");

    sb.append("  Hashcode: ");
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
    builder.append(this.activity);
    builder.append(this.outcome);
    builder.append(this.runtime);
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
    builder.append(this.activity, transaction.activity);
    builder.append(this.runtime, transaction.runtime);
    builder.append(this.outcome, transaction.outcome);
    builder.append(this.stackTrace, transaction.stackTrace);

    return builder.isEquals();
  }

}
