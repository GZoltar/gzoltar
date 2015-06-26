package pt.up.fe.aes.base.spectrum;

import java.util.List;

import pt.up.fe.aes.base.model.Tree;

public interface Spectrum {

	public int getComponentsSize();

	public int getTransactionsSize();

	public boolean isInvolved(int t, int c);

	public boolean isError(int t);

	public Tree getTree();
	
	public void print();

	List<Integer> getTestFrequencyPerProbe();

	List<Integer> getTestFrequencyPerNode();
}
