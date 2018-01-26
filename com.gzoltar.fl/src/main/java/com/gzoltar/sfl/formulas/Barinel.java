package com.gzoltar.sfl.formulas;

import com.gzoltar.sfl.AbstractSFL;

/**
 * Implementation of Barinel coefficient from <i></i>
 * 
 * @author José Campos
 */
public final class Barinel extends AbstractSFL {

  @Override
  public String getName() {
    return "Barinel";
  }

  @Override
  public double compute(final double n00, final double n01, final double n10, final double n11) {
    if (n10 + n11 == 0.0) {
      return 0.0;
    }
    double h = n10 / (n10 + n11);
    return Math.pow(h, n10) * Math.pow(1 - h, 11);
  }
}
