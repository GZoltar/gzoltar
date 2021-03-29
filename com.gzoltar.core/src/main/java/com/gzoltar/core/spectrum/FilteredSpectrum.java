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
package com.gzoltar.core.spectrum;

import org.apache.commons.lang3.tuple.ImmutablePair;
import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.instr.Outcome;
import com.gzoltar.core.instr.actions.BlackList;
import com.gzoltar.core.instr.actions.WhiteList;
import com.gzoltar.core.instr.filter.Filter;
import com.gzoltar.core.instr.granularity.GranularityLevel;
import com.gzoltar.core.instr.matchers.ClassNameMatcher;
import com.gzoltar.core.instr.matchers.MethodAnnotationMatcher;
import com.gzoltar.core.instr.matchers.MethodModifierMatcher;
import com.gzoltar.core.instr.matchers.MethodNameMatcher;
import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.NodeType;
import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.runtime.Probe;
import com.gzoltar.core.runtime.ProbeGroup;
import com.gzoltar.core.util.ArrayUtils;
import javassist.Modifier;

public class FilteredSpectrum {

  private final GranularityLevel granularity;

  private final Filter classFilter;

  private final Filter methodFilter = new Filter();

  /**
   * 
   * @param source
   */
  public FilteredSpectrum(AgentConfigs configs) {

    this.granularity = configs.getGranularity();

    // === Class level filters ===

    // instrument some classes
    WhiteList includeClasses = new WhiteList(new ClassNameMatcher(configs.getIncludes()));

    // do not instrument some classes
    BlackList excludeClasses = new BlackList(new ClassNameMatcher(configs.getExcludes()));

    this.classFilter = new Filter(includeClasses, excludeClasses);

    // === Method level filters ===

    if (!configs.getInclPublicMethods()) {
      this.methodFilter.add(new BlackList(new MethodModifierMatcher(Modifier.PUBLIC)));
    }

    if (!configs.getInclStaticConstructors()) {
      this.methodFilter.add(new BlackList(new MethodNameMatcher("<clinit>*")));
    }

    if (!configs.getInclDeprecatedMethods()) {
      this.methodFilter
          .add(new BlackList(new MethodAnnotationMatcher(Deprecated.class.getCanonicalName())));
    }
  }

  /**
   * Returns a filtered {@link com.gzoltar.core.spectrum.ISpectrum} object according to user's
   * preferences.
   * 
   * @param source
   * @return
   */
  public ISpectrum filter(ISpectrum source) {
    if (source == null) {
      return null;
    }

    ISpectrum filteredSpectrum = new Spectrum();

    // === Filter probeGroups and probes ===

    for (ProbeGroup probeGroup : source.getProbeGroups()) {
      // does 'probeGroup' match any filter?
      if (this.classFilter.filter(probeGroup.getCtClass()) == Outcome.REJECT) {
        continue;
      }

      ProbeGroup newProbeGroup = new ProbeGroup(probeGroup.getHash(), probeGroup.getCtClass());

      Filter granularityMethodFilter = new Filter();
      for (Probe probe : probeGroup.getProbes()) {
        // does 'probe' match any filter?
        if (this.methodFilter.filter(probe.getCtBehavior()) == Outcome.REJECT) {
          continue;
        }

        // === Skip nodes according to a granularity level ===

        if (granularityMethodFilter.filter(probe.getCtBehavior()) == Outcome.REJECT) {
          continue;
        }

        if (this.granularity == GranularityLevel.LINE) {
          // register Line probe
          newProbeGroup.registerProbe(probe.getNode(), probe.getCtBehavior());
        } else if (this.granularity == GranularityLevel.CLASS) {
          // register Class probe
          newProbeGroup.registerProbe(probe.getNode(), probe.getCtBehavior());
          break;
        } else if (this.granularity == GranularityLevel.METHOD) {
          // register Method probe
          newProbeGroup.registerProbe(probe.getNode(), probe.getCtBehavior());

          Node node = probe.getNode();
          String methodName =
              node.getName().substring(node.getName().indexOf(NodeType.METHOD.getSymbol()) + 1,
                  node.getName().indexOf(NodeType.LINE.getSymbol()));

          granularityMethodFilter.add(new BlackList(new MethodNameMatcher(methodName)));
        } else if (this.granularity == GranularityLevel.BASICBLOCK && probe.getNode().isStartBlock()) {
          // register BasicBlock probe
          newProbeGroup.registerProbe(probe.getNode(), probe.getCtBehavior());
        }
      }

      if (!newProbeGroup.isEmpty()) {
        filteredSpectrum.addProbeGroup(newProbeGroup);
      }
    }

    // === Filter transactions ===

    for (Transaction transaction : source.getTransactions()) {
      Transaction newTransaction =
          new Transaction(transaction.getName(), transaction.getTransactionOutcome(),
              transaction.getRuntime(), transaction.getStackTrace());

      for (String hash : transaction.getProbeGroupsHash()) {
        if (!filteredSpectrum.containsProbeGroupByHash(hash)) {
          // probeGroup has been ignored, therefore it could also be ignore here
          continue;
        }

        // shrink hitArray

        ProbeGroup probeGroup = source.getProbeGroupByHash(hash);
        boolean[] hitArray = transaction.getHitArrayByProbeGroupHash(hash);

        ProbeGroup newProbeGroup = filteredSpectrum.getProbeGroupByHash(hash);
        boolean[] newHitArray = new boolean[newProbeGroup.getNumberOfProbes()];

        for (Probe probe : probeGroup.getProbes()) {
          Probe newProbe = newProbeGroup.findProbeByNode(probe.getNode());
          if (newProbe == null) {
            // probe has been removed, therefore it could also be ignore here
            continue;
          }

          newHitArray[newProbe.getArrayIndex()] = hitArray[probe.getArrayIndex()];
        }

        if (ArrayUtils.containsValue(newHitArray, true)) {
          newTransaction.addActivity(hash,
              new ImmutablePair<String, boolean[]>(newProbeGroup.getName(), newHitArray));
        }
      }

      // check whether it has any activation is performed in the method itself
      filteredSpectrum.addTransaction(newTransaction);
    }

    // === Reset name and type according to a granularity level ===

    if (this.granularity != GranularityLevel.LINE) {
      for (ProbeGroup probeGroup : filteredSpectrum.getProbeGroups()) {
        for (Probe probe : probeGroup.getProbes()) {
          Node node = probe.getNode();

          String newNodeName = null;
          NodeType newNodeType = null;

          switch (this.granularity) {
            case CLASS:
              newNodeName =
                  node.getName().substring(0, node.getName().indexOf(NodeType.METHOD.getSymbol()));
              newNodeType = NodeType.CLASS;
              break;
            case METHOD:
              newNodeName =
                  node.getName().substring(0, node.getName().indexOf(NodeType.LINE.getSymbol()));
              newNodeType = NodeType.METHOD;
              break;
            case BASICBLOCK:
            case LINE:
            default:
              break;
          }

          if (newNodeName != null && newNodeType != null) {
            node.setName(newNodeName);
            node.setNodeType(newNodeType);
          }
        }
      }
    }

    return filteredSpectrum;
  }

}
