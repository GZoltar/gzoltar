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
package com.gzoltar.report.fl.formatter.html;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;
import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.fl.IFormula;

public class FaultLocalizationHTMLReport extends AbstractHTMLReport {

  private static final String GZOLTAR_JS = "gzoltar.js";

  private final List<HTMLViews> views;

  /**
   * 
   * @param view
   */
  public FaultLocalizationHTMLReport(final List<HTMLViews> views) {
    this.views = views;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void generateFaultLocalizationReport(final File outputDirectory, final ISpectrum spectrum,
      final List<IFormula> formulas) throws IOException {
    for (IFormula formula : formulas) {
      // for each formula creates a directory with a HTML report

      File formulaOutputDirectory = new File(
          outputDirectory + File.separator + formula.getName().toLowerCase(Locale.ENGLISH));

      if (!formulaOutputDirectory.exists()) {
        formulaOutputDirectory.mkdirs();
      }

      // Copy GZoltar.js
      PrintWriter gzWriter =
          new PrintWriter(formulaOutputDirectory + File.separator + GZOLTAR_JS, "UTF-8");
      gzWriter.write(this.readFile(GZOLTAR_JS));
      gzWriter.close();

      // Create a JSON string with the content of Spectrum
      String json = this.toJSON(spectrum, formula);

      // Feed the HTML view(s) with the JSON
      for (HTMLViews view : this.views) {
        String htmlContent = this.readFile(view.getFileName());
        String html = String.format(htmlContent, "var root = JSON.parse(" + "'" + json + "');");

        PrintWriter htmlWriter =
            new PrintWriter(formulaOutputDirectory + File.separator + view.getFileName(), "UTF-8");
        htmlWriter.write(html);
        htmlWriter.close();
      }
    }
  }
}
