package com.gzoltar.core.runtime;

import java.util.LinkedHashMap;
import java.util.Map;
import com.gzoltar.core.events.IEventListener;
import com.gzoltar.core.events.MultiEventListener;
import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.model.TransactionOutcome;
import com.gzoltar.core.spectrum.Spectrum;
import com.gzoltar.core.util.ArrayUtils;

public class Collector {

  private static Collector collector;

  private MultiEventListener listener;

  private final Spectrum spectrum;

  /**
   * 
   * @return
   */
  public static Collector instance() {
    if (collector == null) {
      collector = new Collector();
    }
    return collector;
  }

  /**
   * 
   */
  public static void restart() {
    if (collector != null) {
      Collector newCollector = new Collector();
      newCollector.listener = collector.listener;
      collector = newCollector;
    }
  }

  /**
   * 
   * @param listener
   */
  private Collector() {
    this.listener = new MultiEventListener();
    this.spectrum = new Spectrum();
  }

  /**
   * 
   * @param listener
   */
  public void addListener(final IEventListener listener) {
    if (listener != null) {
      this.listener.add(listener);
    }
  }

  /**
   * 
   * @return
   */
  public Spectrum getSpectrum() {
    return this.spectrum;
  }

  /**
   * 
   * @param probeGroup
   */
  public synchronized void regiterProbeGroup(final ProbeGroup probeGroup) {
    if (probeGroup.isEmpty()) {
      return;
    }

    this.spectrum.addProbeGroup(probeGroup);
    this.listener.regiterProbeGroup(probeGroup);
  }

  /**
   * 
   * @param probeGroupHash
   * @return
   */
  public synchronized ProbeGroup getProbeGroup(final String probeGroupHash) {
    return this.spectrum.getProbeGroup(probeGroupHash);
  }

  /**
   * 
   * @param transactionName
   * @param outcome
   * @param runtime
   * @param stackTrace
   */
  public synchronized void endTransaction(final String transactionName,
      final TransactionOutcome outcome, final long runtime, final String stackTrace) {

    // collect coverage
    Map<String, ProbeGroup> probeGroups = new LinkedHashMap<String, ProbeGroup>();
    for (ProbeGroup probeGroup : this.spectrum.getProbeGroups()) {
      if (!ArrayUtils.containsValue(probeGroup.getHitArray(), true)) {
        continue;
      }

      try {
        probeGroups.put(probeGroup.getHash(), (ProbeGroup) probeGroup.clone());
      } catch (CloneNotSupportedException e) {
        throw new RuntimeException(e);
      }

      // reset probes
      this.spectrum.resetHitArray(probeGroup.getHash());
    }

    // create a new transaction
    Transaction transaction = new Transaction(transactionName, probeGroups, outcome, runtime, stackTrace);
    this.spectrum.addTransaction(transaction);
    // and inform all listeners
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
   * @param args
   */
  public synchronized void getHitArray(final Object[] args) {
    assert args.length == 3;

    final String hash = (String) args[0];
    //final String probeGroupName = (String) args[1];
    final Integer numberOfProbes = Integer.valueOf((String) args[2]);

    if (!this.spectrum.containsProbeGroup(hash)) {
      args[0] = new boolean[numberOfProbes];
    } else {
      args[0] = this.spectrum.getHitArray(hash);
    }
  }

  /**
   * In violation of the regular semantic of {@link Object#equals(Object)} this implementation is
   * used as the interface to the runtime data.
   * 
   * @param args the arguments as an {@link Object} array
   * @return has no meaning
   */
  @Override
  public boolean equals(final Object args) {
    if (args instanceof Object[]) {
      this.getHitArray((Object[]) args);
    }
    return super.equals(args);
  }
}
