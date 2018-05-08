package com.gzoltar.core.model;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Transaction implements Serializable {

  private static final long serialVersionUID = 6409327525875082943L;

  private final String name;

  private final Set<Node> activity;

  private final TransactionOutcome outcome;

  private final long runtime;

  private final String stackTrace;

  /**
   * 
   * @param name
   * @param activity
   * @param isError
   */
  public Transaction(final String name, final Set<Node> activity, final TransactionOutcome outcome,
      final long runtime, final String stackTrace) {
    this.name = name;
    this.activity = activity;
    this.outcome = outcome;
    this.runtime = runtime;
    this.stackTrace = stackTrace;
  }

  /**
   * 
   * @return
   */
  public String getName() {
    return this.name;
  }

  /**
   * 
   * @return
   */
  public boolean hasActivations() {
    return !this.activity.isEmpty();
  }

  /**
   * 
   * @return
   */
  public Set<Node> getActivity() {
    return this.activity;
  }

  /**
   * 
   * @return
   */
  public int getNumberActivities() {
    return this.activity.size();
  }

  /**
   * 
   * @param node
   * @return
   */
  public boolean isNodeActived(Node node) {
    return this.activity.contains(node);
  }

  /**
   * 
   * @return
   */
  public TransactionOutcome getTransactionOutcome() {
    return this.outcome;
  }

  /**
   * 
   * @return
   */
  public boolean hasFailed() {
    return this.outcome.equals(TransactionOutcome.FAIL);
  }

  /**
   * 
   * @return
   */
  public long getRuntime() {
    return this.runtime;
  }

  /**
   * 
   * @return
   */
  public String getStackTrace() {
    return this.stackTrace;
  }

  /**
   * Returns a multi-line stack trace in one line and removes potentially-variable formatting.
   * 
   * @return
   */
  public String getNormalizedStackTrace() {
    return this.stackTrace.replaceAll(" *\r?\n[ \t]*", " ") // kill newlines and surrounding space
        .replaceAll("^[ \t\r\n]*|[ \t\r\n]*$", ""); // strip whitespace
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
    builder.append(this.outcome, transaction.outcome);
    builder.append(this.stackTrace, transaction.stackTrace);

    return builder.isEquals();
  }
}
