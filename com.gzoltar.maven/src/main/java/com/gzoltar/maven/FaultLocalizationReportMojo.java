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
package com.gzoltar.maven;

import java.net.URL;
import java.util.List;
import java.util.Locale;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.reporting.MavenReportException;
import com.gzoltar.maven.utils.ClasspathUtils;
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
      URL[] testClasspathURLs = ClasspathUtils.getTestClasspath(this.getProject());
      ClasspathUtils.setClassLoaderClasspath(testClasspathURLs);

      // build a fault localization report
      FaultLocalizationReportBuilder.build(this.getProject().getBuild().getOutputDirectory(),
          this.createAgentConfigurations(), this.outputDirectory, this.dataFile, this.flFamilies);

    } catch (Exception e) {
      throw new MavenReportException(e.toString(), e);
    }
  }
}
