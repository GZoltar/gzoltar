package com.gzoltar.report.metrics.experimental;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

import com.gzoltar.report.metrics.AbstractMetric;

public class DistinctTransactionsRho extends AbstractMetric {

	@Override
	public double calculate() {

		if(!validMatrix())
			return 0;

		Map<Integer, BitSet> distinctTransactionSet = new HashMap<Integer, BitSet>();

		for (int t = 0; t < spectrum.getTransactionsSize(); t++) {
			distinctTransactionSet.put(getHash(t), spectrum.getTransactionActivity(t));
		}

		int components = spectrum.getComponentsSize();
		int transactions = distinctTransactionSet.size();
		int activity_counter = 0;

		for(BitSet activity : distinctTransactionSet.values()) {
			activity_counter += activity.cardinality();
		}

		double rho = (double) activity_counter / ( ((double) components) * ((double) transactions) );
		return rho;
	}

	@Override
	public String getName() {
		return "Distinct Transactions Rho";
	}

	protected int getHash(int t) {
		return spectrum.getTransactionActivity(t).hashCode();
	}

	public static class GlobalDistinctTransactionsRho extends DistinctTransactionsRho {
		@Override
		protected int getHash(int t) {
			return spectrum.getTransactionHashCode(t);
		}
	}
}
