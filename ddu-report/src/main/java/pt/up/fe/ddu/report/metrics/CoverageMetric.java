package pt.up.fe.ddu.report.metrics;


public class CoverageMetric extends AbstractMetric {

	private String granularity;
	
	public CoverageMetric(String granularity) {
		setGranularity(granularity);
	}

	public void setGranularity(String granularity) {
		this.granularity = granularity;
	}
	
	@Override
	public double calculate() {
		
		if(!validMatrix())
			return 0;
		
		int components = spectrum.getComponentsSize();
		int activations = 0;
		
		for (int c = 0; c < components; c++) {
			
			for (int t = 0; t < spectrum.getTransactionsSize(); t++) {
				if (spectrum.isInvolved(t, c)) {
					activations += 1;
					break;
				}
			}
			
		}
		
		double coverage = (double) activations / (double) components;
		
		return coverage;
	}

	@Override
	public String getName() {
		String name = "Coverage";
		if (granularity != null) {
			name += " [ " + granularity + " ]";
		}
		return name;
	}

}
