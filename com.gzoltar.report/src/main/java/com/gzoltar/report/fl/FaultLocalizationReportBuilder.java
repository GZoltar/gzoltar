package com.gzoltar.report.fl;

import java.io.File;
import java.util.List;
import java.util.Locale;
import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.fl.FaultLocalization;
import com.gzoltar.report.IReportFormatter;
import com.gzoltar.report.fl.config.ConfigFaultLocalizationFamily;
import com.gzoltar.report.fl.config.ConfigFaultLocalizationReport;
import com.gzoltar.report.fl.config.ConfigHTMLReportFormatter;
import com.gzoltar.report.fl.formatter.IFaultLocalizationReportFormatter;
import com.gzoltar.report.fl.formatter.html.FaultLocalizationHTMLReport;
import com.gzoltar.report.fl.formatter.txt.FaultLocalizationTxtReport;

public class FaultLocalizationReportBuilder {

  public static void build(final String buildLocation, final AgentConfigs agentConfigs,
      final File outputDirectory, final File dataFile,
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
      ISpectrum spectrum = fl.diagnose(buildLocation, agentConfigs, dataFile);

      // which formatter of report?
      for (IReportFormatter formatter : flFamily.getFormatters()) {
        File formatterOutputDirectory = new File(familyOutputDirectory + File.separator
            + formatter.getReportFormatter().name().toLowerCase(Locale.ENGLISH));

        IFaultLocalizationReportFormatter report = null;
        switch (formatter.getReportFormatter()) {
          case TXT:
          default:
            report = new FaultLocalizationTxtReport();
            break;
          case HTML:
            report =
                new FaultLocalizationHTMLReport(((ConfigHTMLReportFormatter) formatter).getHtmlViews());
            break;
        }

        // then report it
        FaultLocalizationReport flReport = new FaultLocalizationReport(formatterOutputDirectory,
            flFamily.getMetrics(), flFamily.getFormulas(), report);
        flReport.generateReport(spectrum);
      }
    }
  }
}
