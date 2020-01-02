/**
 * Copyright (C) 2020 GZoltar contributors.
 * 
 * This file is part of GZoltar.
 * 
 * GZoltar is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * GZoltar is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with GZoltar. If
 * not, see <https://www.gnu.org/licenses/>.
 */
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
