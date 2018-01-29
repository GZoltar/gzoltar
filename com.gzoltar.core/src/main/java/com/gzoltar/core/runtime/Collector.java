package com.gzoltar.core.runtime;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import com.gzoltar.core.events.IEventListener;
import com.gzoltar.core.events.MultiEventListener;
import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.NodeType;
import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.spectrum.SpectrumListener;

public class Collector {

  private static Collector collector;

  private final MultiEventListener listener;

  private final SpectrumListener spectrumListener;

  private final Map<String, ProbeGroup> probeGroups;

  /**
   * 
   * @return
   */
  public static Collector instance() {
    return collector;
  }

  /**
   * 
   * @param listener
   */
  public static void start(final IEventListener listener) {
    if (collector == null) {
      collector = new Collector(listener);
    }
  }

  /**
   * 
   */
  public static void restart() {
    if (collector != null) {
      collector = new Collector(collector.listener);
    }
  }

  /**
   * 
   * @param listener
   */
  private Collector(final IEventListener listener) {
    this.listener = new MultiEventListener();
    this.addListener(listener);
    this.spectrumListener = new SpectrumListener();
    this.addListener(this.spectrumListener);

    this.probeGroups = new LinkedHashMap<String, ProbeGroup>();
  }

  /**
   * 
   * @param listener
   */
  private void addListener(final IEventListener listener) {
    if (listener != null) {
      this.listener.add(listener);
    }
  }

  /**
   * 
   * @return
   */
  public SpectrumListener getSpectrumListener() {
    return this.spectrumListener;
  }

  /**
   * 
   * @param parent
   * @param name
   * @param type
   * @return
   */
  public synchronized Node createNode(final Node parent, final String name, final int lineNumber, final NodeType type) {
    Node node = new Node(name, lineNumber, type, parent);
    this.listener.addNode(node);
    return node;
  }

  /**
   * 
   * @param groupName
   * @param nodeId
   * @return
   */
  public synchronized Probe regiterProbe(final String groupName, final Node node) {
    ProbeGroup probeGroup = this.probeGroups.get(groupName);
    if (probeGroup == null) {
      probeGroup = new ProbeGroup(groupName);
      this.probeGroups.put(groupName, probeGroup);
    }

    return probeGroup.registerProbe(node);
  }

  /**
   * 
   */
  public synchronized void startTransaction() {
    for (ProbeGroup probeGroup : this.probeGroups.values()) {
      probeGroup.resetHitArray();
    }
  }

  /**
   * 
   * @param transactionName
   * @param isError
   */
  public synchronized void endTransaction(final String transactionName, final boolean isError) {
    Set<Node> hitNodes = new LinkedHashSet<Node>();
    for (ProbeGroup probeGroup : this.probeGroups.values()) {
      hitNodes.addAll(probeGroup.getHitNodes());
    }

    Transaction transaction = new Transaction(transactionName, hitNodes, isError);
    this.listener.endTransaction(transaction);
  }

  /**
   * 
   */
  public synchronized void endSession() {
    this.listener.endSession();
  }

  /**
   * 
   * @param className
   * @return
   */
  public synchronized boolean[] getHitArray(final String className) {
    if (!this.probeGroups.containsKey(className)) {
      // registerProbe has not been called before for this groupName therefore we can return null
      return null;
    }
    return this.probeGroups.get(className).getHitArray();
  }

  /**
   * 
   * @param className
   * @return
   */
  public synchronized boolean hasHitArray(final String className) {
    if (!this.probeGroups.containsKey(className)) {
      return false;
    }
    return this.probeGroups.get(className).hasHitArray();
  }

  /**
   * 
   * @return
   */
  public Node getRootNode() {
    return this.spectrumListener.getSpectrum().getTree().getRoot();
  }

}
