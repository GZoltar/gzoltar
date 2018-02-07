package com.gzoltar.report.fl.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import com.gzoltar.report.AbstractReportFormat;
import com.gzoltar.report.ReportFormat;
import com.gzoltar.report.fl.format.html.HTMLViews;

public class ConfigHTMLReportFormat extends AbstractReportFormat {

  private List<HTMLViews> htmlViews = new ArrayList<HTMLViews>();

  /**
   * 
   */
  public ConfigHTMLReportFormat() {
    super(ReportFormat.HTML);

    // default view
    this.htmlViews.add(HTMLViews.SUNBURST);
  }

  /**
   * 
   * @param htmlViews
   */
  public void setHtmlViews(List<String> htmlViews) {
    this.htmlViews.clear();

    for (String htmlView : htmlViews) {
      this.htmlViews.add(HTMLViews.valueOf(htmlView.toUpperCase(Locale.ENGLISH)));
    }
  }

  /**
   * 
   * @return
   */
  public List<HTMLViews> getHtmlViews() {
    return this.htmlViews;
  }

  /**
   * 
   * @return
   */
  public boolean hasHtmlViews() {
    return !this.htmlViews.isEmpty();
  }
}
