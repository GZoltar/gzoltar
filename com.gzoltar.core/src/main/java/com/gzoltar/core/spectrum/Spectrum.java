package com.gzoltar.core.spectrum;

import java.util.ArrayList;
import java.util.List;
import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.model.TransactionOutcome;
import com.gzoltar.core.model.Tree;

public class Spectrum implements ISpectrum {

  private Tree tree;

  private final List<Transaction> transactions;

  /**
   * 
   */
  public Spectrum() {
    this.tree = new Tree();
    this.transactions = new ArrayList<Transaction>();
  }

  /**
   * {@inheritDoc}
   */
  public void setTree(Tree tree) {
    this.tree = tree;
  }

  /**
   * {@inheritDoc}
   */
  public Tree getTree() {
    return this.tree;
  }

  /**
   * {@inheritDoc}
   */
  public void addNode(Node node) {
    this.tree.addNode(node);
  }

  /**
   * {@inheritDoc}
   */
  public List<Node> getTargetNodes() {
    return this.tree.getTargetNodes();
  }

  /**
   * {@inheritDoc}
   */
  public int getNumberOfTargetNodes() {
    return this.tree.getNumberOfTargetNodes();
  }

  /**
   * {@inheritDoc}
   */
  public void addTransaction(final Transaction transaction) {
    if (transaction.hasActivations()) {
      this.transactions.add(transaction);
    }
  }

  /**
   * {@inheritDoc}
   */
  public List<Transaction> getTransactions() {
    return this.transactions;
  }

  /**
   * {@inheritDoc}
   */
  public int getNumberOfTransactions() {
    return this.transactions.size();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(this.tree.toString());
    sb.append("\n");

    for (Transaction transaction : this.transactions) {
      sb.append(transaction.getName() + "\t");
      for (Node node : this.getTargetNodes()) {
        if (transaction.isNodeActived(node)) {
          sb.append("1 ");
        } else {
          sb.append("0 ");
        }
      }

      if (transaction.hasFailed()) {
        sb.append(TransactionOutcome.FAIL.getSymbol());
      } else {
        sb.append(TransactionOutcome.PASS.getSymbol());
      }

      sb.append(" hashcode: " + transaction.hashCode());
      sb.append("\n");
    }

    sb.append("\nNumber of target nodes: " + this.getNumberOfTargetNodes() + " / " + this.tree.getNumberOfNodes() + "\n");
    sb.append("Number of transactions: " + this.transactions.size() + "\n");

    return sb.toString();
  }
}
