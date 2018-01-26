package com.gzoltar.sfl.formulas;

import com.gzoltar.sfl.AbstractSFL;

/**
 * Implementation of Russel and Rao coefficient from <i>On habitat and association of species of
 * anophelinae larvae in south-eastern Madras</i>
 * 
 * @author José Campos
 */
public final class RusselRao extends AbstractSFL {

  @Override
  public String getName() {
    return "RusselRao";
  }

  @Override
  public double compute(final double n00, final double n01, final double n10, final double n11) {
    if (n11 + n01 + n10 + n00 == 0) {
      return 0.0;
    }
    return n11 / (n11 + n01 + n10 + n00);
  }
}
