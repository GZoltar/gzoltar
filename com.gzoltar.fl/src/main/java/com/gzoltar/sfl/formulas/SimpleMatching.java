package com.gzoltar.sfl.formulas;

import com.gzoltar.sfl.AbstractSFL;

/**
 * Implementation of Simple-matching coefficient from <i>A Statistical Method for Evaluating
 * Systematic Relationships</i>
 * 
 * @author José Campos
 */
public final class SimpleMatching extends AbstractSFL {

  @Override
  public String getName() {
    return "SimpleMatching";
  }

  @Override
  public double compute(final double n00, final double n01, final double n10, final double n11) {
    if (n11 + n01 + n10 + n00 == 0) {
      return 0.0;
    }
    return (n11 + n00) / (n11 + n01 + n10 + n00);
  }
}
