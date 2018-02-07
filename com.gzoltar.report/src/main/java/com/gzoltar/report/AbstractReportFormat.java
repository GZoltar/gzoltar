package com.gzoltar.report;

public abstract class AbstractReportFormat implements IReportFormat {

  private ReportFormat reportFormat;

  protected AbstractReportFormat(ReportFormat reportFormat) {
    this.reportFormat = reportFormat;
  }

  /**
   * {@inheritDoc}
   */
  public ReportFormat getReportFormat() {
    return this.reportFormat;
  }
}
