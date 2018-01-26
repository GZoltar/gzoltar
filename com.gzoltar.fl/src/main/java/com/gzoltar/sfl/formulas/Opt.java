package com.gzoltar.sfl.formulas;

import com.gzoltar.sfl.AbstractSFL;

/**
 * Implementation of Opt coefficient from <i></i>
 * 
 * @author Rui Abreu
 */
public final class Opt extends AbstractSFL {

  @Override
  public String getName() {
    return "Opt";
  }

  @Override
  public double compute(final double n00, final double n01, final double n10, final double n11) {
    return n11 - (n10 / (n10 + n00 + 1.0));
  }
}
