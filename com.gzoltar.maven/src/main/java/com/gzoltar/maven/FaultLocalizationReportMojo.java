package com.gzoltar.maven;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.reporting.MavenReportException;
import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.fl.FaultLocalization;
import com.gzoltar.fl.FaultLocalizationFamily;
import com.gzoltar.fl.IFormula;
import com.gzoltar.report.IReport;
import com.gzoltar.report.ReportFormat;
import com.gzoltar.report.fl.FaultLocalizationHTMLReport;
import com.gzoltar.report.fl.FaultLocalizationReport;
import com.gzoltar.report.fl.FaultLocalizationTxtReport;
import com.gzoltar.report.fl.HTMLViews;
import com.gzoltar.sfl.SFLFormulas;

@Mojo(name = "fl-report", defaultPhase = LifecyclePhase.VERIFY, threadSafe = true)
public class FaultLocalizationReportMojo extends AbstractReportMojo {

  /**
   * Output directory for the reports.
   */
  @Parameter(property = "gzoltar.outputDirectory",
      defaultValue = "${project.reporting.outputDirectory}/gzoltar")
  private File outputDirectory;

  /**
   * File with execution data.
   */
  @Parameter(property = "gzoltar.dataFile",
      defaultValue = "${project.build.directory}/gzoltar.exec")
  private File dataFile;

  /**
   * Fault localization report format. Valid options are:
   * <ul>
   * <li>txt: text based fault localization reports (default)</li>
   * <li>html: graphical fault localization reports in HTML</li>
   * </ul>
   */
  @Parameter(property = "gzoltar.format", defaultValue = "txt")
  protected String format;

  @Parameter(property = "gzoltar.flFamily", defaultValue = "SFL")
  private String flFamily;

  @Parameter
  private List<String> formulas;

  @Parameter(property = "gzoltar.htmlView", defaultValue = "SUNBURST")
  private String htmlView;

  @Override
  public String getDescription(final Locale locale) {
    return this.getName(locale) + " Fault Localization Report.";
  }

  @Override
  protected boolean canGenerateReportRegardingDataFiles() {
    return this.dataFile.exists();
  }

  @Override
  protected void executeReport(final Locale locale) throws MavenReportException {
    ReportFormat reportFormat = this.isReportFormatValid();
    if (reportFormat == null) {
      getLog().error("Invalid format type '" + this.format + "'. Valid values are:");
      for (ReportFormat rF : ReportFormat.values()) {
        getLog().info("  " + rF.name());
      }
      return;
    }

    FaultLocalizationFamily faultLocalizationFamily = this.isFLFamilyValid();
    if (faultLocalizationFamily == null) {
      getLog().error("Invalid fault localization family '" + this.flFamily.toString()
          + "'. Valid values are:");
      for (FaultLocalizationFamily flf : FaultLocalizationFamily.values()) {
        getLog().info("  " + flf.name());
      }
      return;
    }

    List<IFormula> iFormulas = this.areFormulasValid();
    if (iFormulas == null) {
      getLog().error("Invalid formulas type '" + this.formulas.toString() + "'. Valid values are:");
      // TODO group other type of formulas not only SFL ones
      for (SFLFormulas f : SFLFormulas.values()) {
        getLog().info("  " + f.name());
      }
      return;
    }

    HTMLViews htmlView = this.ishtmlViewValid();
    if (htmlView == null) {
      getLog().error("Invalid HTMLView type '" + this.htmlView + "'. Valid values are:");
      for (HTMLViews v : HTMLViews.values()) {
        getLog().info("  " + v.name());
      }
      return;
    }

    try {
      // first diagnose it
      FaultLocalization fl = new FaultLocalization(faultLocalizationFamily, iFormulas);
      ISpectrum spectrum = fl.diagnose(this.dataFile);

      // which format of report?
      IReport report = null;
      switch (reportFormat) {
        case TXT:
        default:
          report = new FaultLocalizationTxtReport(this.outputDirectory, iFormulas);
          break;
        case HTML:
          report = new FaultLocalizationHTMLReport(this.outputDirectory, iFormulas, htmlView);
          break;
      }

      // then report it
      FaultLocalizationReport flReport = new FaultLocalizationReport(report);
      flReport.generateReport(spectrum);
    } catch (IOException e) {
      getLog().info(e);
      e.printStackTrace();
    }
  }

  @Override
  protected String getOutputDirectory() {
    return this.outputDirectory.getAbsolutePath();
  }

  /**
   * 
   * @return a ReportFormat object if format is valid, null otherwise.
   */
  public ReportFormat isReportFormatValid() {
    try {
      return ReportFormat.valueOf(this.format.toUpperCase(Locale.ENGLISH));
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  /**
   * 
   * @return a FaultLocalizationFamily object if family is valid, null otherwise.
   */
  public FaultLocalizationFamily isFLFamilyValid() {
    try {
      return FaultLocalizationFamily.valueOf(this.flFamily.toUpperCase(Locale.ENGLISH));
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  /**
   * 
   * @return a list of IFormula objects if all formulas are valid, null otherwise.
   */
  public List<IFormula> areFormulasValid() {
    List<IFormula> iFormulas = new ArrayList<IFormula>();
    for (String formula : this.formulas) {
      try {
        iFormulas.add(SFLFormulas.valueOf(formula.toUpperCase(Locale.ENGLISH)).getFormula());
        // TODO check for other type of formulas not only SFL
      } catch (IllegalArgumentException e) {
        return null;
      }
    }
    return iFormulas;
  }

  /**
   * 
   * @return a HTMLViews object if htmlView is valid, null otherwise
   */
  public HTMLViews ishtmlViewValid() {
    try {
      return HTMLViews.valueOf(this.htmlView.toUpperCase(Locale.ENGLISH));
    } catch (IllegalArgumentException e) {
      return null;
    }
  }
}
