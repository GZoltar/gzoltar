package com.gzoltar.maven;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.reporting.MavenReportException;
import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.fl.FaultLocalization;
import com.gzoltar.report.IReportFormat;
import com.gzoltar.report.fl.FaultLocalizationReport;
import com.gzoltar.report.fl.config.ConfigFaultLocalizationFamily;
import com.gzoltar.report.fl.config.ConfigFaultLocalizationReport;
import com.gzoltar.report.fl.config.ConfigHTMLReportFormat;
import com.gzoltar.report.fl.format.IFaultLocalizationReportFormat;
import com.gzoltar.report.fl.format.html.FaultLocalizationHTMLReport;
import com.gzoltar.report.fl.format.txt.FaultLocalizationTxtReport;

@Mojo(name = "fl-report", defaultPhase = LifecyclePhase.VERIFY, threadSafe = true)
public final class FaultLocalizationReportMojo extends AbstractReportMojo {

  @Parameter(property = "gzoltar.flFamilies")
  private List<ConfigFaultLocalizationFamily> flFamilies;

  @Override
  public String getDescription(final Locale locale) {
    return this.getName(locale) + " Fault Localization Report.";
  }

  @Override
  protected void executeReport(final Locale locale) throws MavenReportException {
    // in case there is not any configuration defined, use a default one
    if (this.flFamilies == null || this.flFamilies.isEmpty()) {
      this.flFamilies = ConfigFaultLocalizationReport.getDefaults();
    }

    try {
      for (ConfigFaultLocalizationFamily flFamily : this.flFamilies) {
        if (flFamily.getFaultLocalizationFamily() == null) {
          // name is mandatory
          throw new MavenReportException("<flFamily> tag requires a <name> tag");
        }

        ConfigFaultLocalizationReport.setDefaultsIfNotPresent(this.flFamilies);

        String familyOutputDirectory = this.outputDirectory.getAbsolutePath() + File.separator
            + flFamily.getName().toLowerCase(Locale.ENGLISH);

        // first diagnose it
        FaultLocalization fl =
            new FaultLocalization(flFamily.getFaultLocalizationFamily(), flFamily.getFormulas());
        ISpectrum spectrum = fl.diagnose(this.dataFile);

        // which format of report?
        for (IReportFormat format : flFamily.getFormats()) {
          File formatOutputDirectory = new File(familyOutputDirectory + File.separator
              + format.getReportFormat().name().toLowerCase(Locale.ENGLISH));

          IFaultLocalizationReportFormat report = null;
          switch (format.getReportFormat()) {
            case TXT:
            default:
              report = new FaultLocalizationTxtReport();
              break;
            case HTML:
              report = new FaultLocalizationHTMLReport(((ConfigHTMLReportFormat) format).getHtmlViews());
              break;
          }

          // then report it
          FaultLocalizationReport flReport = new FaultLocalizationReport(formatOutputDirectory, flFamily.getMetrics(), flFamily.getFormulas(), report);
          flReport.generateReport(spectrum);
        }
      }
    } catch (IOException e) {
      throw new MavenReportException(e.toString());
    }
  }
}
