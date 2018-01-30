package com.gzoltar.report.fl;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;
import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.fl.IFormula;

public class FaultLocalizationHTMLReport extends AbstractHTMLReport {

  private static final String GZOLTAR_JS = "gzoltar.js";

  private final HTMLViews view;

  /**
   * 
   * @param outputDirectory
   * @param formulas
   * @param view
   */
  public FaultLocalizationHTMLReport(final File outputDirectory, final List<IFormula> formulas,
      final HTMLViews view) {
    super(outputDirectory, formulas);
    this.view = view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void generateReport(final ISpectrum spectrum) throws IOException {
    for (IFormula formula : this.formulas) {
      // for each formula creates a directory with a HTML report

      File formulaOutputDirectory =
          new File(this.outputDirectory + File.separator + formula.getName().toLowerCase(Locale.ENGLISH));

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

      // Feed the HTML view with the JSON
      String htmlContent = this.readFile(this.view.getFileName());
      String html = String.format(htmlContent, "var root = JSON.parse(" + "'" + json + "');");

      PrintWriter htmlWriter = new PrintWriter(
          formulaOutputDirectory + File.separator + this.view.getFileName(), "UTF-8");
      htmlWriter.write(html);
      htmlWriter.close();
    }
  }
}
