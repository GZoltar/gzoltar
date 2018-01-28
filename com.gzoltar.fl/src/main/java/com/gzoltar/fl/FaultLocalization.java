package com.gzoltar.fl;

import java.util.Locale;
import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.sfl.SFLFormulas;

/**
 * 
 * @author José Campos
 */
public final class FaultLocalization {

  public static void diagnose(final ISpectrum spectrum, final IFL... fls) {
    for (IFL fl : fls) {
      fl.diagnose(spectrum);
    }
  }

  public static void diagnose(final ISpectrum spectrum, final String... flsNames) {
    for (String flName : flsNames) {
      IFL fl = SFLFormulas.valueOf(flName.toUpperCase(Locale.ENGLISH)).getFormula();
      fl.diagnose(spectrum);
    }
  }
}
