package com.gzoltar.sfl.formulas;

import com.gzoltar.sfl.AbstractSFL;

/**
 * Implementation of Tarantula coefficient from <i>Visualization of Test Information to Assist Fault
 * Localization</i>
 * 
 * @author Rui Abreu
 */
public final class Tarantula extends AbstractSFL {

  @Override
  public String getName() {
    return "Tarantula";
  }

  @Override
  public double compute(final double n00, final double n01, final double n10, final double n11) {
    double nFailed = n11 + n01 == 0 ? 0 : n11 / (n11 + n01);
    double nPassed = n10 + n00 == 0 ? 0 : n10 / (n10 + n00);

    if (nFailed + nPassed == 0) {
      return 0.0;
    } else {
      return nFailed / (nFailed + nPassed);
    }
  }
}
