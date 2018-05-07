package com.gzoltar.maven;

import java.util.List;
import java.util.Locale;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.reporting.MavenReportException;
import com.gzoltar.report.fl.FaultLocalizationReportBuilder;
import com.gzoltar.report.fl.config.ConfigFaultLocalizationFamily;

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
    try {

      // build a fault localization report
      FaultLocalizationReportBuilder.build(this.outputDirectory, this.dataFile, this.flFamilies);

    } catch (Exception e) {
      throw new MavenReportException(e.toString(), e);
    }
  }
}
