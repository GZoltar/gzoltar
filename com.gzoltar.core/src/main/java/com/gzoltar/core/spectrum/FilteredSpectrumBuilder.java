package com.gzoltar.core.spectrum;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.Tree;

public class FilteredSpectrumBuilder extends SpectrumListener {

  private ISpectrum source;

  private HashSet<Integer> includeNodes;

  private HashSet<Integer> excludeNodes;

  public FilteredSpectrumBuilder() {
    this.resetIncludesExcludes();
  }

  public FilteredSpectrumBuilder setSource(final ISpectrum source) {
    this.source = source;
    return this;
  }

  public FilteredSpectrumBuilder includeNode(final Node node) {
    if (node == null) {
      return this;
    }
    return this.includeNode(node.getId());
  }

  public FilteredSpectrumBuilder includeNode(final int nodeId) {
    this.includeNodes.add(nodeId);
    return this;
  }

  public FilteredSpectrumBuilder excludeNode(final Node node) {
    if (node == null) {
      return this;
    }
    return this.excludeNode(node.getId());
  }

  public FilteredSpectrumBuilder excludeNode(final int nodeId) {
    this.excludeNodes.add(nodeId);
    return this;
  }

  public void resetIncludesExcludes() {
    this.includeNodes = new HashSet<Integer>();
    this.excludeNodes = new HashSet<Integer>();
  }

  @Override
  public ISpectrum getSpectrum() {
    if (this.source == null) {
      return null;
    }

    this.resetSpectrum();

    Tree tree = this.source.getTree();
    this.spectrum.setTree(tree);

    // filter probes
    List<Integer> newProbeMapping = new ArrayList<Integer>();
    for (int probeId = 0; probeId < this.source.getComponentsSize(); probeId++) {
      Node node = this.source.getNodeOfProbe(probeId);

      if (isIncluded(node) && !isExcluded(node)) {
        int newProbeId = newProbeMapping.size();
        newProbeMapping.add(probeId);
        this.addProbe(newProbeId, node.getId());
      }
    }

    // filter tests
    int numberOfProbes = newProbeMapping.size();

    if (numberOfProbes > 0) {
      for (int t = 0; t < this.source.getTransactionsSize(); t++) {
        String transactionName = this.source.getTransactionName(t);
        boolean isError = this.source.isError(t);
        boolean newActivity[] = new boolean[numberOfProbes];
        BitSet oldActivity = this.source.getTransactionActivity(t);
        int hashCode = this.source.getTransactionHashCode(t);

        for (int p = 0; p < numberOfProbes; p++) {
          int oldProbeId = newProbeMapping.get(p);
          newActivity[p] = oldActivity.get(oldProbeId);
        }

        this.endTransaction(transactionName, newActivity, hashCode, isError);
      }
    }

    return spectrum;
  }

  private boolean isIncluded(final Node node) {
    return this.find(this.includeNodes, node);
  }

  private boolean isExcluded(final Node node) {
    return this.find(this.excludeNodes, node);
  }

  private boolean find(final Set<Integer> set, final Node node) {
    if (node == null) {
      return false;
    } else if (set.contains(node.getId())) {
      return true;
    } else {
      return this.find(set, node.getParent());
    }
  }
}
