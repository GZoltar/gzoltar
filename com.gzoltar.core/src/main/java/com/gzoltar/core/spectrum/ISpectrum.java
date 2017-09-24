package com.gzoltar.core.spectrum;

import java.util.BitSet;
import java.util.List;
import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.model.Tree;

public interface ISpectrum {

  public int getComponentsSize();

  public int getTransactionsSize();

  public boolean isInvolved(final int transactionId, final int componentID);

  public boolean isError(final int transactionId);

  public Tree getTree();

  public void print();

  public List<Integer> getTestFrequencyPerProbe();

  public List<Integer> getTestFrequencyPerNode();

  public BitSet getTransactionActivity(final int transactionId);

  public String getTransactionName(final int transactionId);

  public List<Integer> getActiveComponentsInTransaction(final int transactionId);

  public Node getNodeOfProbe(final int probeId);

  public int getTransactionHashCode(final int transactionId);

  public double getMinCompTrans(final int componentID);

  public double getMaxCompTrans(final int componentID);

  public int getProbeOfNode(final int nodeId);

  public List<Transaction> getTransactions();

}
