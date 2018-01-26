package com.gzoltar.sfl.formulas;

import com.gzoltar.sfl.AbstractSFL;

/**
 * Implementation of Jaccard coefficient from <i>Étude comparative de la distribution florale dans
 * une portion des Alpes et des Jura</i>
 * 
 * @author José Campos
 */
public final class Jaccard extends AbstractSFL {

  @Override
  public String getName() {
    return "Jaccard";
  }

  @Override
  public double compute(final double n00, final double n01, final double n10, final double n11) {
    if (n11 + n01 + n10 == 0) {
      return 0.0;
    }
    return n11 / (n11 + n01 + n10);
  }
}
