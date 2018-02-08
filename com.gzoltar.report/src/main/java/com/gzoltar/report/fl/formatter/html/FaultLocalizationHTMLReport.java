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
