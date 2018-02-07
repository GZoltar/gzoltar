package com.gzoltar.report.metrics;

import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.fl.IFormula;

public interface IMetric {

  /**
   * 
   * @return
   */
  public double calculate(final ISpectrum spectrum);

  /**
   * 
   * @return
   */
  public String getName();

  /**
   * 
   * @return
   */
  public boolean requireFormula();

  /**
   * 
   * @param formula
   */
  public void setFormula(IFormula formula);

  /**
   * 
   * @return
   */
  public IFormula getFormula();
}
