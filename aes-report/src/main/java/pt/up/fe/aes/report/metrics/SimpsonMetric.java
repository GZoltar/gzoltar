package pt.up.fe.aes.report.metrics;

import java.util.LinkedHashMap;

public class SimpsonMetric extends AbstractMetric {

	@Override
	public double calculate() {
		
		if(!validMatrix())
			return 0;
		
		LinkedHashMap<Integer, Integer> species = new LinkedHashMap<Integer, Integer>();
		for (int t = 0; t < spectrum.getTransactionsSize(); t++) {
			int hash = getHash(t);
			if (species.containsKey(hash)) {
				species.put(hash, species.get(hash) + 1);
			}
			else {
				species.put(hash, 1);
			}
		}
		
		double n = 0.0;
		double N = 0.0;
		for (int s : species.keySet()) {
			double ni = species.get(s);

			n += (ni * (ni - 1));
			N += ni;
		}

		double diversity = ( (N == 0.0) || ((N - 1) == 0) ) ? 1.0 : n / (N * (N - 1));
		return diversity;
	}
	
	protected int getHash(int t) {
		return spectrum.getTransactionActivity(t).hashCode();
	}
	
	@Override
	public String getName() {
		return "Simpson";
	}
	
	public static class InvertedSimpsonMetric extends SimpsonMetric {
		@Override
		public double calculate() {
			return 1d - super.calculate();
		}
		
		@Override
		public String getName() {
			return "Inverted Simpson";
		}
	}
	
	public static class GlobalInvertedSimpsonMetric extends InvertedSimpsonMetric {
		@Override
		protected int getHash(int t) {
			return spectrum.getTransactionHashCode(t);
		}
		
		@Override
		public String getName() {
			return "Inverted Simpson";
		}
	}
	
	public static class GlobalSimpsonMetric extends SimpsonMetric {
		@Override
		protected int getHash(int t) {
			return spectrum.getTransactionHashCode(t);
		}
	}

}
