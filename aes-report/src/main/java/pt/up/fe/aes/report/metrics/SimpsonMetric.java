package pt.up.fe.aes.report.metrics;

import java.util.LinkedHashMap;

public class SimpsonMetric extends AbstractMetric {

	@Override
	public double calculate() {
		
		if(!validMatrix())
			return 0;
		
		LinkedHashMap<Integer, Integer> species = new LinkedHashMap<Integer, Integer>();
		for (int t = 0; t < spectrum.getTransactionsSize(); t++) {
			int hash = spectrum.getTransactionActivity(t).hashCode();
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

}
