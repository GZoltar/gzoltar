package com.gzoltar.core.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import com.gzoltar.core.util.SerialisationIdentifiers;

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
   * Serialises an instance of {@link com.gzoltar.core.model.Transaction}.
   * 
   * @param out binary stream to write bytes to
   */
  public void serialize(final DataOutputStream out) {
    if (this.hasActivations()) {
      try {
        out.writeByte(SerialisationIdentifiers.BLOCK_TRANSACTION);
        out.writeUTF(this.getName());
        out.writeInt(this.getNumberActivities());
        for (Node node : this.getActivity()) {
          out.writeUTF(node.getName());
        }
        out.writeUTF(this.getTransactionOutcome().name());
        out.writeLong(this.getRuntime());
        out.writeUTF(this.getStackTrace());
      } catch (final IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  /**
   * Deserialises and create an instance of {@link com.gzoltar.core.model.Transaction}.
   * 
   * @param in binary stream to read bytes from
   * @param tree a {@link com.gzoltar.core.model.Tree} object
   * @return a {@link com.gzoltar.core.model.Transaction} object
   * @throws IOException
   */
  public static Transaction deserialize(final DataInputStream in, final Tree tree) throws IOException {
    String name = in.readUTF();

    Set<Node> activity = new LinkedHashSet<Node>();
    int numberActivities = in.readInt();
    while (numberActivities > 0) {
      Node node = tree.getNode(in.readUTF());
      assert node != null;
      activity.add(node);

      numberActivities--;
    }

    TransactionOutcome transactionOutcome = TransactionOutcome.valueOf(in.readUTF());
    long runtime = in.readLong();
    String stackTrace = in.readUTF();

    return new Transaction(name, activity, transactionOutcome, runtime, stackTrace);
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
