package com.gzoltar.core.spectrum;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.Tree;

public class FilteredSpectrumBuilder extends SpectrumBuilder {

  private Spectrum source;

  private HashSet<Integer> includeNodes;
  private HashSet<Integer> excludeNodes;

  public FilteredSpectrumBuilder() {
    resetIncludesExcludes();
  }

  public FilteredSpectrumBuilder setSource(Spectrum source) {
    this.source = source;
    return this;
  }

  public FilteredSpectrumBuilder includeNode(Node node) {
    if (node == null)
      return this;
    return includeNode(node.getId());
  }

  public FilteredSpectrumBuilder includeNode(int nodeId) {
    includeNodes.add(nodeId);
    return this;
  }

  public FilteredSpectrumBuilder excludeNode(Node node) {
    if (node == null)
      return this;
    return excludeNode(node.getId());
  }

  public FilteredSpectrumBuilder excludeNode(int nodeId) {
    excludeNodes.add(nodeId);
    return this;
  }

  public void resetIncludesExcludes() {
    includeNodes = new HashSet<Integer>();
    excludeNodes = new HashSet<Integer>();
  }

  @Override
  public Spectrum getSpectrum() {
    if (source == null)
      return null;

    resetSpectrum();

    Tree tree = source.getTree();
    spectrum.setTree(tree);

    // filter probes
    List<Integer> newProbeMapping = new ArrayList<Integer>();

    for (int probeId = 0; probeId < source.getComponentsSize(); probeId++) {

      Node node = source.getNodeOfProbe(probeId);

      if (isIncluded(node) && !isExcluded(node)) {
        int newProbeId = newProbeMapping.size();
        newProbeMapping.add(probeId);

        addProbe(newProbeId, node.getId());
      }
    }

    // filter tests
    int numberOfProbes = newProbeMapping.size();

    if (numberOfProbes > 0) {
      for (int t = 0; t < source.getTransactionsSize(); t++) {
        String transactionName = source.getTransactionName(t);
        boolean isError = source.isError(t);
        boolean newActivity[] = new boolean[numberOfProbes];
        BitSet oldActivity = source.getTransactionActivity(t);
        int hashCode = source.getTransactionHashCode(t);

        for (int p = 0; p < numberOfProbes; p++) {
          int oldProbeId = newProbeMapping.get(p);
          newActivity[p] = oldActivity.get(oldProbeId);
        }

        endTransaction(transactionName, newActivity, hashCode, isError);
      }
    }
    return spectrum;
  }

  private boolean isIncluded(Node node) {
    return find(this.includeNodes, node);
  }

  private boolean isExcluded(Node node) {
    return find(this.excludeNodes, node);
  }

  private static boolean find(Set<Integer> set, Node node) {
    if (node == null) {
      return false;
    } else if (set.contains(node.getId())) {
      return true;
    } else {
      return find(set, node.getParent());
    }
  }
}
