package com.gzoltar.report.fl;

import java.util.List;
import com.gzoltar.fl.IFormula;
import com.gzoltar.report.IReport;

public interface IFaultLocalizationReport extends IReport {

  /**
   * 
   * @return
   */
  public List<IFormula> getFormulas();
}
