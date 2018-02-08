package com.gzoltar.report;

public abstract class AbstractReportFormatter implements IReportFormatter {

  private ReportFormatter reportFormatter;

  protected AbstractReportFormatter(ReportFormatter reportFormat) {
    this.reportFormatter = reportFormat;
  }

  /**
   * {@inheritDoc}
   */
  public ReportFormatter getReportFormatter() {
    return this.reportFormatter;
  }
}
