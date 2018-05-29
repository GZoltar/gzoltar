package com.gzoltar.core.runtime;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
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

  /** <ProbeGroup hash, hitArray> */
  private final Map<String, Pair<String, boolean[]>> hitArrays;

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
    this.hitArrays = new LinkedHashMap<String, Pair<String, boolean[]>>();
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
   * @param probeGroup
   * @return
   */
  public synchronized ProbeGroup getProbeGroup(final ProbeGroup probeGroup) {
    return this.spectrum.getProbeGroup(probeGroup);
  }

  /**
   * 
   * @param probeGroupHash
   * @return
   */
  public synchronized ProbeGroup getProbeGroupByHash(final String probeGroupHash) {
    return this.spectrum.getProbeGroupByHash(probeGroupHash);
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
    Map<String, Pair<String, boolean[]>> activity =
        new LinkedHashMap<String, Pair<String, boolean[]>>();
    for (Entry<String, Pair<String, boolean[]>> entry : this.hitArrays.entrySet()) {

      boolean[] hitArray = entry.getValue().getRight();
      if (!ArrayUtils.containsValue(hitArray, true)) {
        // no coverage
        continue;
      }

      String hash = entry.getKey();
      boolean[] cloneHitArray = new boolean[hitArray.length];
      System.arraycopy(hitArray, 0, cloneHitArray, 0, hitArray.length);
      activity.put(hash,
          new ImmutablePair<String, boolean[]>(entry.getValue().getLeft(), cloneHitArray));

      // reset probes
      for (int i = 0; i < hitArray.length; i++) {
        hitArray[i] = false;
      }
    }

    // create a new transaction
    Transaction transaction = new Transaction(transactionName, activity, outcome, runtime, stackTrace);
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
    final String probeGroupName = (String) args[1];
    final Integer numberOfProbes = Integer.valueOf((String) args[2]);

    if (!this.hitArrays.containsKey(hash)) {
      this.hitArrays.put(hash,
          new ImmutablePair<String, boolean[]>(probeGroupName, new boolean[numberOfProbes]));
    }

    args[0] = this.hitArrays.get(hash).getRight();
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
