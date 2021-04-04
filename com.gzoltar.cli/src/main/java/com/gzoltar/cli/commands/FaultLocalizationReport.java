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
package com.gzoltar.cli.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.kohsuke.args4j.Option;
import com.gzoltar.core.instr.granularity.GranularityLevel;
import com.gzoltar.fl.FaultLocalizationFamily;
import com.gzoltar.report.IReportFormatter;
import com.gzoltar.report.ReportFormatter;
import com.gzoltar.report.fl.FaultLocalizationReportBuilder;
import com.gzoltar.report.fl.FaultLocalizationReportFormatterFactory;
import com.gzoltar.report.fl.config.ConfigFaultLocalizationFamily;
import com.gzoltar.report.metrics.Metric;
import com.gzoltar.sfl.SFLFormulas;

/**
 * The <code>faultLocalizationReport</code> command.
 */
public class FaultLocalizationReport extends AbstractReport {

  @Option(name = "--granularity", usage = "source code granularity level of report",
      metaVar = "<line|method|class>", required = false)
  private String granularity = GranularityLevel.LINE.name();

  @Option(name = "--inclPublicMethods",
      usage = "specifies whether public methods of each class should be reported",
      metaVar = "<boolean>", required = false)
  private Boolean inclPublicMethods = true;

  @Option(name = "--inclStaticConstructors",
      usage = "specifies whether public static constructors of each class should be reported",
      metaVar = "<boolean>", required = false)
  private Boolean inclStaticConstructors = false;

  @Option(name = "--inclDeprecatedMethods",
      usage = "specifies whether methods annotated as deprecated should be reported",
      metaVar = "<boolean>", required = false)
  private Boolean inclDeprecatedMethods = true;

  @Option(name = "--family", usage = "fault localization family", metaVar = "<family>",
      required = false)
  private String family = FaultLocalizationFamily.SFL.name();

  @Option(name = "--formula", usage = "fault localization formula (use ':' to define more than one formula)", metaVar = "<formula>",
      required = false)
  private String formula = SFLFormulas.OCHIAI.name();

  @Option(name = "--metric", usage = "fault localization ranking metric (use ':' to define more than one metric)", metaVar = "<metric>",
      required = false)
  private String metric = Metric.AMBIGUITY.name();

  @Option(name = "--formatter", usage = "fault localization report formatter (use ':' to define more than one formatter)",
      metaVar = "<formatter>", required = false)
  private String formatter = ReportFormatter.TXT.name();

  /**
   * {@inheritDoc}
   */
  @Override
  public String name() {
    return "faultLocalizationReport";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String description() {
    return "Create a fault localization report based on previously collected data.";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void generateReport(final Locale locale) throws Exception {
    this.agentConfigs.setGranularity(this.granularity);
    this.agentConfigs.setInclPublicMethods(this.inclPublicMethods);
    this.agentConfigs.setInclStaticConstructors(this.inclStaticConstructors);
    this.agentConfigs.setInclDeprecatedMethods(this.inclDeprecatedMethods);
    this.agentConfigs.setIncludes(this.includes);
    this.agentConfigs.setExcludes(this.excludes);

    final ConfigFaultLocalizationFamily configFlFamily = new ConfigFaultLocalizationFamily();

    // set fault localization family
    configFlFamily
        .setFaultLocalizationFamily(
            FaultLocalizationFamily.valueOf(this.family.toUpperCase(locale)));
    // set formula
    configFlFamily.setFormulas(Arrays.asList(this.formula.split("\\:")));
    // set metrics
    configFlFamily.setMetrics(Arrays.asList(this.metric.split("\\:")));
    // set formatters
    List<IReportFormatter> formatters = new ArrayList<IReportFormatter>();
    for (String formatter : this.formatter.split("\\:")) {
      formatters.add(FaultLocalizationReportFormatterFactory
          .createReportFormatter(ReportFormatter.valueOf(formatter.toUpperCase(Locale.ENGLISH))));
    }
    configFlFamily.setFormatters(formatters);

    // build a fault localization report
    FaultLocalizationReportBuilder.build(this.buildLocation.getAbsolutePath(), this.agentConfigs,
        this.outputDirectory, this.dataFile, Arrays.asList(configFlFamily));
  }
}
