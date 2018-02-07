package com.gzoltar.report.fl;

import java.io.File;
import java.util.List;
import java.util.Locale;
import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.fl.FaultLocalization;
import com.gzoltar.report.IReportFormat;
import com.gzoltar.report.fl.config.ConfigFaultLocalizationFamily;
import com.gzoltar.report.fl.config.ConfigFaultLocalizationReport;
import com.gzoltar.report.fl.config.ConfigHTMLReportFormat;
import com.gzoltar.report.fl.format.IFaultLocalizationReportFormat;
import com.gzoltar.report.fl.format.html.FaultLocalizationHTMLReport;
import com.gzoltar.report.fl.format.txt.FaultLocalizationTxtReport;

public class FaultLocalizationReportBuilder {

  public static void build(final File outputDirectory, final File dataFile,
      List<ConfigFaultLocalizationFamily> flFamilies) throws Exception {

    // in case there is not any configuration defined, use a default one
    if (flFamilies == null || flFamilies.isEmpty()) {
      flFamilies = ConfigFaultLocalizationReport.getDefaults();
    }

    for (ConfigFaultLocalizationFamily flFamily : flFamilies) {
      if (flFamily.getFaultLocalizationFamily() == null) {
        // name is mandatory
        throw new Exception("<flFamily> tag requires a <name> tag");
      }

      ConfigFaultLocalizationReport.setDefaultsIfNotPresent(flFamilies);

      String familyOutputDirectory = outputDirectory.getAbsolutePath() + File.separator
          + flFamily.getName().toLowerCase(Locale.ENGLISH);

      // first diagnose it
      FaultLocalization fl =
          new FaultLocalization(flFamily.getFaultLocalizationFamily(), flFamily.getFormulas());
      ISpectrum spectrum = fl.diagnose(dataFile);

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
            report =
                new FaultLocalizationHTMLReport(((ConfigHTMLReportFormat) format).getHtmlViews());
            break;
        }

        // then report it
        FaultLocalizationReport flReport = new FaultLocalizationReport(formatOutputDirectory,
            flFamily.getMetrics(), flFamily.getFormulas(), report);
        flReport.generateReport(spectrum);
      }
    }
  }
}
