package pt.up.fe.aes.report.metrics;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class EntropyMetric extends AbstractMetric {

	@Override
	public double calculate() {
		
		if(!validMatrix())
			return 0;
		
		LinkedHashMap<Integer, Integer> counter = new LinkedHashMap<Integer, Integer>();
		
		for(int t = 0; t < spectrum.getTransactionsSize(); t++) {
			int hash = spectrum.getTransactionActivity(t).hashCode();
			
			if(counter.containsKey(hash)) {
				counter.put(hash, counter.get(hash) + 1);
			}
			else {
				counter.put(hash, 1);
			}
		}
		
		double transactions = spectrum.getTransactionsSize();
		double components = spectrum.getComponentsSize();
		
		double entropy = 0.0;
		for(Entry<Integer, Integer> entry : counter.entrySet()) {
			double prob_i =  (double) entry.getValue() / transactions;
			entropy += prob_i * log2(prob_i);
		}
		
		entropy = -entropy / components;

		return entropy;
	}

	private static double log2(double value) {
		return Math.log(value) / Math.log(2);
	}

	@Override
	public String getName() {
		return "Entropy";
	}
}
