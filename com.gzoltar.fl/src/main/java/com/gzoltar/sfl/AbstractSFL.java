package com.gzoltar.sfl;

import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.fl.IFL;

public abstract class AbstractSFL implements IFL {

  @Override
  public void diagnose(final ISpectrum spectrum) {
    int n00 = 0;
    int n01 = 0;
    int n10 = 0;
    int n11 = 0;

    for (Node node : spectrum.getTargetNodes()) {
      n00 = n01 = n10 = n11 = 0;

      for (Transaction transaction : spectrum.getTransactions()) {
        boolean hasFailed = transaction.isError();

        if (transaction.isNodeActived(node)) {
          if (hasFailed) {
            n11++;
          } else {
            n10++;
          }
        } else {
          if (hasFailed) {
            n01++;
          } else {
            n00++;
          }
        }
      }

      node.addSuspiciousnessValue(this.getName(), this.compute(n00, n01, n10, n11));
    }
  }

  /**
   * Returns the name of the SBL formula
   * 
   * @return
   */
  public abstract String getName();

  /**
   * Returns a suspiciousness value
   * 
   * @param n00 number of passing test cases that do not execute the faulty node
   * @param n01 number of failing test cases that do not execute the faulty node
   * @param n10 number of passing test cases that execute the faulty node
   * @param n11 number of failing test cases that execute the faulty node
   * @return
   */
  public abstract double compute(final double n00, final double n01, final double n10,
      final double n11);
}
