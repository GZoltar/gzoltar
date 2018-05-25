package com.gzoltar.core.spectrum;

import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.instr.Outcome;
import com.gzoltar.core.instr.actions.BlackList;
import com.gzoltar.core.instr.actions.WhiteList;
import com.gzoltar.core.instr.filter.Filter;
import com.gzoltar.core.instr.matchers.ClassNameMatcher;
import com.gzoltar.core.instr.matchers.MethodAnnotationMatcher;
import com.gzoltar.core.instr.matchers.MethodModifierMatcher;
import com.gzoltar.core.instr.matchers.MethodNameMatcher;
import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.runtime.Probe;
import com.gzoltar.core.runtime.ProbeGroup;
import javassist.Modifier;

public class FilteredSpectrum {

  // private final GranularityLevel granularity;

  private final Filter classFilter;

  private final Filter methodFilter = new Filter();

  /**
   * 
   * @param source
   */
  public FilteredSpectrum(AgentConfigs configs) {

    // TODO not clear yet how to implement this
    // IGranularity granularity = GranularityFactory.getGranularity(ctClass, methodInfo,
    // configs.getGranularity());

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

      for (Probe probe : probeGroup.getProbes()) {
        // does 'probe' match any filter?
        if (this.methodFilter.filter(probe.getCtBehavior()) == Outcome.REJECT) {
          continue;
        }

        newProbeGroup.registerProbe(probe.getNode(), probe.getCtBehavior());
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

        newTransaction.addActivity(hash, newHitArray);
      }

      // check whether it has any activation is performed in the method itself
      filteredSpectrum.addTransaction(newTransaction);
    }

    return filteredSpectrum;
  }

}
