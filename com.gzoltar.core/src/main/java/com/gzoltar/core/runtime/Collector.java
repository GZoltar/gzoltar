package com.gzoltar.core.runtime;

import com.gzoltar.core.events.EventListener;
import com.gzoltar.core.events.MultiEventListener;
import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.Tree;
import com.gzoltar.core.model.Node.Type;
import com.gzoltar.core.runtime.ProbeGroup.HitProbe;
import com.gzoltar.core.spectrum.SpectrumBuilder;

public class Collector {

  private static Collector collector;

  private MultiEventListener listener;
  private Tree tree;
  private HitVector hitVector;
  private SpectrumBuilder builder;

  public static Collector instance() {
    return collector;
  }

  public static void start(EventListener listener) {
    if (collector == null) {
      collector = new Collector(listener);
    }
  }

  private Collector(EventListener listener) {
    this.listener = new MultiEventListener();
    this.builder = new SpectrumBuilder();
    addListener(this.builder);
    addListener(listener);

    this.tree = new Tree();
    this.hitVector = new HitVector();
  }

  public void addListener(EventListener listener) {
    if (listener != null) {
      this.listener.add(listener);
    }
  }

  public SpectrumBuilder getBuilder() {
    return this.builder;
  }

  public synchronized Node createNode(Node parent, String name, Type type) {
    Node node = tree.addNode(name, type, parent.getId());
    listener.addNode(node.getId(), name, type, parent.getId());
    return node;
  }

  public synchronized HitProbe createHitProbe(String groupName, int nodeId) {
    HitProbe p = hitVector.registerProbe(groupName, nodeId);
    listener.addProbe(p.getId(), p.getNodeId());
    return p;
  }

  public synchronized void endTransaction(String transactionName, boolean isError) {
    listener.endTransaction(transactionName, hitVector.get(), isError); // hitVector.get()
  }

  public synchronized void startTransaction() {
    hitVector.reset();
  }

  public synchronized void endSession() {
    // tree.print();
    listener.endSession();
  }

  public synchronized boolean[] getHitVector(String className) {
    return hitVector.get(className);
  }

  public synchronized boolean existsHitVector(String className) {
    return hitVector.existsHitVector(className);
  }

  public Node getRootNode() {
    return tree.getRoot();
  }

}
