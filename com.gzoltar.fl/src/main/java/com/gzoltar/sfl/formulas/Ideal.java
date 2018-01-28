package com.gzoltar.sfl.formulas;

/**
 * Implementation of Ideal coefficient from <i></i>
 *
 * @author Rui Abreu
 */
public final class Ideal extends AbstractSFLFormula {

  @Override
  public String getName() {
    return "Ideal";
  }

  @Override
  public double compute(final double n00, final double n01, final double n10, final double n11) {
    if (n01 > 0.0) {
      return 0.0;
    }
    return n11 / (n11 + n10);
  }
}
