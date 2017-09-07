package com.gzoltar.report.metrics;

public class RhoMetric extends AbstractMetric {
	
	@Override
	public double calculate() {
		
		if(!validMatrix())
			return 0;
		
		int transactions = spectrum.getTransactionsSize();
		int components = spectrum.getComponentsSize();
		
		int activity_counter = 0;
		for(int t = 0; t < transactions; t++) {
			for(int c = 0; c < components; c++) {
				if (spectrum.isInvolved(t, c)) {
					activity_counter++;
				}
			}
		}
		double rho = (double) activity_counter / ( ((double) components) * ((double) transactions) );
		
		return rho;
	}

	@Override
	public String getName() {
		return "Density";
	}
	
	
	public static class NormalizedRho extends RhoMetric {
		@Override
		public double calculate() {
			double rho = super.calculate();
			
			return 1.0 - Math.abs(1.0 - 2.0 * rho);
		}
		
		@Override
		public String getName() {
			return "Normalized Rho";
		}
	}
}
