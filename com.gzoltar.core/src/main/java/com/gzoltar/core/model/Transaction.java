package com.gzoltar.core.model;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang3.builder.EqualsBuilder;

public class Transaction implements Serializable {

  private static final long serialVersionUID = 6409327525875082943L;

  private final String name;

  private final Set<Node> activity;

  private final boolean isError;

  private int hashCode;

  /**
   * 
   * @param name
   * @param activity
   * @param isError
   */
  public Transaction(final String name, final Set<Node> activity, final boolean isError) {
    this.name = name;
    this.activity = activity;
    this.isError = isError;
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
      final boolean isError) {
    this(name, activity, isError);
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
  public boolean isError() {
    return this.isError;
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
    builder.append(this.isError, transaction.isError);
    builder.append(this.hashCode, transaction.hashCode);

    return builder.isEquals();
  }
}
