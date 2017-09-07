package com.gzoltar.report.metrics;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class EntropyMetric extends AbstractMetric {

	protected LinkedHashMap<Integer, Integer> globalCounter = new LinkedHashMap<Integer, Integer>();
	protected LinkedHashMap<Integer, Integer> localCounter = new LinkedHashMap<Integer, Integer>();

	@Override
	public double calculate() {

		if(!validMatrix())
			return 0;

		globalCounter.clear();
		localCounter.clear();

		for(int t = 0; t < spectrum.getTransactionsSize(); t++) {
			fillCounter(spectrum.getTransactionHashCode(t), globalCounter);
			fillCounter(spectrum.getTransactionActivity(t).hashCode(), localCounter);
		}

		double transactions = spectrum.getTransactionsSize();
		double components = getComponentsSize();

		double entropy = 0.0;
		for(Entry<Integer, Integer> entry : getCounter().entrySet()) {
			double prob_i =  (double) entry.getValue() / transactions;
			entropy += prob_i * log2(prob_i);
		}

		entropy = Math.abs( entropy / components );

		return entropy;
	}

	private static void fillCounter(int hash, LinkedHashMap<Integer, Integer> counter) {
		if(counter.containsKey(hash)) {
			counter.put(hash, counter.get(hash) + 1);
		}
		else {
			counter.put(hash, 1);
		}
	}

	private static double log2(double value) {
		return Math.log(value) / Math.log(2);
	}

	protected int getComponentsSize() {
		return spectrum.getComponentsSize();
	}

	protected LinkedHashMap<Integer, Integer> getCounter() {
		return localCounter;
	}

	@Override
	public String getName() {
		return "Entropy";
	}

	public static class GlobalEntropyMetric extends EntropyMetric {
		@Override
		protected int getComponentsSize() {
			int delta = globalCounter.size() - localCounter.size();
			return spectrum.getComponentsSize() + delta;
		}

		@Override
		protected LinkedHashMap<Integer, Integer> getCounter() {
			return globalCounter;
		}
	}
}
