package com.gzoltar.ant.fl.config;

import java.util.ArrayList;
import java.util.List;
import org.apache.tools.ant.types.resources.Union;
import com.gzoltar.report.IReportFormatter;
import com.gzoltar.report.fl.config.ConfigHTMLReportFormatter;
import com.gzoltar.report.fl.config.ConfigTxtReportFormatter;

public class Formatters extends Union {

  private final List<IReportFormatter> formatters = new ArrayList<IReportFormatter>();

  /**
   * 
   * @return
   */
  public ConfigTxtReportFormatter createTxt() {
    final ConfigTxtReportFormatter element = new ConfigTxtReportFormatter();
    this.formatters.add(element);
    return element;
  }

  /**
   * 
   * @return
   */
  public ConfigHTMLReportFormatter createHtml() {
    final ConfigHTMLReportFormatter element = new ConfigHTMLReportFormatter();
    this.formatters.add(element);
    return element;
  }

  /**
   * 
   * @return
   */
  public List<IReportFormatter> getFormatters() {
    return this.formatters;
  }
}
