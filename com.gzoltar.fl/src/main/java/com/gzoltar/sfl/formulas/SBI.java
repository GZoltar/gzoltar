package com.gzoltar.sfl.formulas;

import com.gzoltar.sfl.AbstractSFL;

/**
 * Implementation of SBI coefficient from <i>Scalable Statistical Bug Isolation</i>
 * 
 * @author José Campos
 */
public final class SBI extends AbstractSFL {

  @Override
  public String getName() {
    return "SBI";
  }

  @Override
  public double compute(final double n00, final double n01, final double n10, final double n11) {
    if (n11 + n01 == 0) {
      return 0.0;
    }
    return n11 / (n11 + n01);
  }
}
