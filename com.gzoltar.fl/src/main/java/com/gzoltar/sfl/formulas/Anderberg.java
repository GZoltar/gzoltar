package com.gzoltar.sfl.formulas;

import com.gzoltar.sfl.AbstractSFL;

/**
 * Implementation of Anderberg coefficient from <i>Clustering analysis for applications</i>
 * 
 * @author José Campos
 */
public final class Anderberg extends AbstractSFL {

  @Override
  public String getName() {
    return "Anderberg";
  }

  @Override
  public double compute(final double n00, final double n01, final double n10, final double n11) {
    if (2 * n11 + n01 + n10 == 0) {
      return 0.0;
    }
    return n11 / (n11 + 2 * (n01 + n10));
  }
}
