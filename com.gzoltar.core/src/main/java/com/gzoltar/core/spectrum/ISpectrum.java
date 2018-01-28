package com.gzoltar.core.spectrum;

import java.util.List;
import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.model.Tree;

public interface ISpectrum {

  /**
   * 
   * @param tree
   */
  public void setTree(Tree tree);

  /**
   * 
   * @return
   */
  public Tree getTree();

  /**
   * 
   * @param node
   */
  public void addNode(Node node);

  /**
   * 
   * @return
   */
  public List<Node> getTargetNodes();

  /**
   * 
   * @return
   */
  public int getNumberOfTargetNodes();

  /**
   * 
   * @param transaction
   */
  public void addTransaction(final Transaction transaction);

  /**
   * 
   * @return
   */
  public List<Transaction> getTransactions();

  /**
   * 
   * @return
   */
  public int getNumberOfTransactions();
}
