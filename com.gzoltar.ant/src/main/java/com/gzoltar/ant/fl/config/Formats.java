package com.gzoltar.ant.fl.config;

import java.util.ArrayList;
import java.util.List;
import org.apache.tools.ant.types.resources.Union;
import com.gzoltar.report.IReportFormat;
import com.gzoltar.report.fl.config.ConfigHTMLReportFormat;
import com.gzoltar.report.fl.config.ConfigTxtReportFormat;

public class Formats extends Union {

  private final List<IReportFormat> formats = new ArrayList<IReportFormat>();

  /**
   * 
   * @return
   */
  public ConfigTxtReportFormat createTxt() {
    final ConfigTxtReportFormat element = new ConfigTxtReportFormat();
    this.formats.add(element);
    return element;
  }

  /**
   * 
   * @return
   */
  public ConfigHTMLReportFormat createHtml() {
    final ConfigHTMLReportFormat element = new ConfigHTMLReportFormat();
    this.formats.add(element);
    return element;
  }

  /**
   * 
   * @return
   */
  public List<IReportFormat> getFormats() {
    return this.formats;
  }
}
