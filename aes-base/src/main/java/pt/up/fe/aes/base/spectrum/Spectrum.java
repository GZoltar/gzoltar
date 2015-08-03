package pt.up.fe.aes.base.spectrum;

import java.util.BitSet;
import java.util.List;

import pt.up.fe.aes.base.model.Node;
import pt.up.fe.aes.base.model.Tree;

public interface Spectrum {

	int getComponentsSize();

	int getTransactionsSize();

	boolean isInvolved(int t, int c);

	boolean isError(int t);

	Tree getTree();
	
	void print();

	List<Integer> getTestFrequencyPerProbe();

	List<Integer> getTestFrequencyPerNode();
	
	BitSet getTransactionActivity(int t);

	String getTransactionName(int t);

	Node getNodeOfProbe(int probeId);
	
	int getTransactionHashCode(int t);
	
	double getMinCompTrans(int c) ;
	
	double getMaxCompTrans(int c) ;
}
