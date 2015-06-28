package pt.up.fe.aes.report.metrics;

import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;


public class AmbiguityMetric extends AbstractMetric {

	@Override
	public double calculate() {
				
		Set<Integer> ambiguityGroups = new HashSet<Integer>();
		
		for(int c = 0; c < spectrum.getComponentsSize(); c++) {
			
			BitSet bs = new BitSet();
			
			for(int t = 0; t < spectrum.getTransactionsSize(); t++) {
				if(spectrum.isInvolved(t, c)) {
					bs.set(t);
				}
			}
			
			ambiguityGroups.add(bs.hashCode());
		}
		
		int components = spectrum.getComponentsSize();
		int groups = ambiguityGroups.size();
		
		double ambiguity = (double) groups / (double) components;
		
		return ambiguity;
	}

	@Override
	public String getName() {
		return "Ambiguity";
	}

}
