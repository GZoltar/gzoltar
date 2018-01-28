package com.gzoltar.sfl.formulas;

/**
 * Implementation of Sorensen-Dice coefficient from <i>Measures of the Amount of Ecologic
 * Association Between Species</i> and <i>A Method of Establishing Groups of Equal Amplitude in
 * Plant Sociology Based on Similarity of Species Content and Its Application to Analyses of the
 * Vegetation on Danish Commons</i>
 * 
 * @author José Campos
 */
public final class SorensenDice extends AbstractSFLFormula {

  @Override
  public String getName() {
    return "SorensenDice";
  }

  @Override
  public double compute(final double n00, final double n01, final double n10, final double n11) {
    if (2 * n11 + n01 + n10 == 0) {
      return 0.0;
    }
    return (2 * n11) / (2 * n11 + n01 + n10);
  }
}
