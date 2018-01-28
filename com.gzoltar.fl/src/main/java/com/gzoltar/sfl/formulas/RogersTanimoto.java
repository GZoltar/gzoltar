package com.gzoltar.sfl.formulas;

/**
 * Implementation of Rogers and Tanimoto coefficient from <i>A Computer Program for Classifying
 * Plants</i>
 * 
 * @author José Campos
 */
public final class RogersTanimoto extends AbstractSFLFormula {

  @Override
  public String getName() {
    return "RogersTanimoto";
  }

  @Override
  public double compute(final double n00, final double n01, final double n10, final double n11) {
    if (n11 + n00 + 2 * (n01 + n10) == 0) {
      return 0.0;
    }
    return (n11 + n00) / (n11 + n00 + 2 * (n01 + n10));
  }
}
