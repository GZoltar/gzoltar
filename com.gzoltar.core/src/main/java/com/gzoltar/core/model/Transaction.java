package com.gzoltar.core.model;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang3.builder.EqualsBuilder;

public class Transaction implements Serializable {

  private static final long serialVersionUID = 6409327525875082943L;

  private final String name;

  private final Set<Node> activity;

  private final TransactionOutcome outcome;

  private int hashCode;

  /**
   * 
   * @param name
   * @param activity
   * @param isError
   */
  public Transaction(final String name, final Set<Node> activity, final TransactionOutcome outcome) {
    this.name = name;
    this.activity = activity;
    this.outcome = outcome;
    this.hashCode = this.activity.hashCode();
  }

  /**
   * 
   * @param name
   * @param activity
   * @param hashCode
   * @param isError
   */
  public Transaction(final String name, final Set<Node> activity, final int hashCode,
      final TransactionOutcome outcome) {
    this(name, activity, outcome);
    this.hashCode = hashCode;
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
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return this.hashCode;
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
    builder.append(this.hashCode, transaction.hashCode);

    return builder.isEquals();
  }
}
