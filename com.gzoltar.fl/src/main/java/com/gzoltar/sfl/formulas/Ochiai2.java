package com.gzoltar.sfl.formulas;

/**
 * Implementation of Ochiai2 coefficient from <i>Zoogeographic studies on the soleoid fishes found
 * in Japan and its neighbouring regions</i>
 * 
 * @author José Campos
 */
public final class Ochiai2 extends AbstractSFLFormula {

  @Override
  public String getName() {
    return "Ochiai2";
  }

  @Override
  public double compute(final double n00, final double n01, final double n10, final double n11) {
    if ((n11 + n01 == 0) || (n11 + n10 == 0) || (n00 + n01 == 0) || (n00 + n10 == 0)) {
      return 0.0;
    }
    return (n11 * n00) / Math.sqrt((n11 + n01) * (n11 + n10) * (n00 + n01) * (n00 + n10));
  }
}
