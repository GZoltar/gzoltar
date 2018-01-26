package com.gzoltar.sfl.formulas;

import com.gzoltar.sfl.AbstractSFL;

/**
 * Implementation of Kulczynski2 coefficient from <i>Classe des Sciences Mathématiques et
 * Naturelles</i>
 * 
 * @author José Campos
 */
public final class Kulczynski2 extends AbstractSFL {

  @Override
  public String getName() {
    return "Kulczynski2";
  }

  @Override
  public double compute(final double n00, final double n01, final double n10, final double n11) {
    if ((n11 + n01 == 0) || (n11 + n10 == 0)) {
      return 0.0;
    }
    return 0.5 * ((n11 / (n11 + n01)) + (n11 / (n11 + n10)));
  }
}
