package com.gzoltar.sfl;

import com.gzoltar.core.model.Node;
import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.fl.IFL;

public abstract class AbstractSFL implements IFL {

  @Override
  public void diagnose(ISpectrum spectrum) {
    int n00 = 0;
    int n01 = 0;
    int n10 = 0;
    int n11 = 0;

    for (int c = 0; c < spectrum.getComponentsSize(); c++) {
      n00 = n01 = n10 = n11 = 0;

      for (int t = 0; t < spectrum.getTransactionsSize(); t++) {
        boolean hasFailed = spectrum.isError(t);

        if (spectrum.isInvolved(t, c)) {
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

      Node node = spectrum.getNodeOfProbe(c);
      node.addSuspiciousnessValues(this.getName(), this.compute(n00, n01, n10, n11));
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
   * @param n00 number of passing test cases that do not execute the faulty statement
   * @param n01 number of failing test cases that do not execute the faulty statement
   * @param n10 number of passing test cases that execute the faulty statement
   * @param n11 number of failing test cases that execute the faulty statement
   * @return
   */
  public abstract double compute(final double n00, final double n01, final double n10,
      final double n11);
}
