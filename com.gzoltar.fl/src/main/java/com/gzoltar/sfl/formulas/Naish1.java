package com.gzoltar.sfl.formulas;

/**
 * Implementation of Naish1 coefficient from <i></i>
 *
 * @author Rui Abreu
 */
public final class Naish1 extends AbstractSFLFormula {

  @Override
  public String getName() {
    return "Naish1";
  }

  @Override
  public double compute(final double n00, final double n01, final double n10, final double n11) {
    if (n01 > 0.0) {
      return -1.0;
    }
    return n00;
  }
}
