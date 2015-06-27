package pt.up.fe.aes.report.metrics;

public class RhoMetric extends AbstractMetric {

	@Override
	public double calculate() {
		
		int transactions = spectrum.getTransactionsSize();
		if (transactions == 0)
			return 0;
		
		int components = spectrum.getComponentsSize();
		if (components == 0)
			return 0;
		
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
	
}
