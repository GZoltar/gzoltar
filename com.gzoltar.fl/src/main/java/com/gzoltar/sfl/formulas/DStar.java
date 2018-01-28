package com.gzoltar.sfl.formulas;

/**
 * Implementation of DStar coefficient from <i></i>
 * 
 * @author Rui Abreu
 */
public final class DStar extends AbstractSFLFormula {

  @Override
  public String getName() {
    return "DStar";
  }

  @Override
  public double compute(final double n00, final double n01, final double n10, final double n11) {
    if ((n10 + n01 == 0) || (n11 == 0)) {
      return 0.0;
    }
    return Math.pow(n11, 2.0) / (n10 + n01);
  }
}
