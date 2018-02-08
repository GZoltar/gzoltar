package com.gzoltar.report.fl.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import com.gzoltar.report.AbstractReportFormatter;
import com.gzoltar.report.ReportFormatter;
import com.gzoltar.report.fl.formatter.html.HTMLViews;

public class ConfigHTMLReportFormatter extends AbstractReportFormatter {

  private List<HTMLViews> htmlViews = new ArrayList<HTMLViews>();

  /**
   * 
   */
  public ConfigHTMLReportFormatter() {
    super(ReportFormatter.HTML);

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
   * @param htmlViews
   */
  public void setViews(String htmlViews) {
    this.setHtmlViews(Arrays.asList(htmlViews.split(":")));
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
