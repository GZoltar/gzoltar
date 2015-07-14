package pt.up.fe.aes.base.spectrum;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.up.fe.aes.base.model.Node;
import pt.up.fe.aes.base.model.Tree;

public class SpectrumImpl implements Spectrum {

	private Tree tree;
	private ArrayList<Integer> probes;
	private ArrayList<Transaction> transactions;
	
	public SpectrumImpl() {
		tree = new Tree();
		probes = new ArrayList<Integer>();
		transactions = new ArrayList<Transaction>();
	}
	
	@Override
	public int getComponentsSize() {
		return probes.size();
	}

	@Override
	public int getTransactionsSize() {
		return transactions.size();
	}

	@Override
	public boolean isInvolved(int t, int c) {
		return transactions.get(t).activity.get(c);
	}

	@Override
	public boolean isError(int t) {
		return transactions.get(t).isError;
	}
	
	public class Transaction {
		public final String name;
		public final BitSet activity;
		public final boolean isError;
		
		public Transaction(String name, boolean[] activityArray, boolean isError) {
			this.name = name;
			this.activity = new BitSet(activityArray.length);
			this.isError = isError;
			
			for (int i = 0; i < activityArray.length; i++) {
				if(activityArray[i])
					activity.set(i);
			}
		}
		
		public boolean hasActivations() {
			return activity.cardinality() != 0;
		}
	}

	public void addTransaction(String transactionName, boolean[] activity, boolean isError) {
		Transaction t = new Transaction(transactionName, activity, isError);
		if (t.hasActivations()) {
			transactions.add(t);
		}
	}
	
	@Override
	public Tree getTree() {
		return tree;
	}
	
	public void setTree(Tree tree) {
		this.tree = tree;
	}

	public void addProbe(int id, int nodeId) {
		probes.ensureCapacity(id + 1);
		
		while(probes.size() <= id) {
			probes.add(null);
		}
		
		probes.set(id, nodeId);
	}

	@Override
	public List<Integer> getTestFrequencyPerProbe() {
		ArrayList<Integer> testFrequency = new ArrayList<Integer>();
		testFrequency.ensureCapacity(getComponentsSize());
		
		for(int p = 0; p < getComponentsSize(); p++) {
			
			Set<Integer> s = new HashSet<Integer>();
			
			for(Transaction t : transactions) {
				if(t.activity.get(p)) {
					s.add(t.activity.hashCode());
				}
			}
			
			testFrequency.add(s.size());
		}
		
		return testFrequency;
	}
	
	@Override
	public List<Integer> getTestFrequencyPerNode() {
		
		ArrayList<Integer> nodeTestFrequency = new ArrayList<Integer>();
		nodeTestFrequency.ensureCapacity(getComponentsSize());
		
		for(int i = 0; i < tree.size(); i++) {
			nodeTestFrequency.add(0);
		}
		
		List<Integer> testFrequency = getTestFrequencyPerProbe();
		
		for(int p = 0; p < getComponentsSize(); p++) {
			int freq = testFrequency.get(p);
			int nodeId = probes.get(p);
			
			nodeTestFrequency.set(nodeId, freq + nodeTestFrequency.get(nodeId));
		}
		
		return nodeTestFrequency;
	}
	
	public void print() {
		tree.print();
		System.out.println("Probe mapping:" + probes);
		
		for(int t = 0; t < getTransactionsSize(); t++) {
			for(int c = 0; c < getComponentsSize(); c++) {
				if(isInvolved(t,c)) {
					System.out.print("1 ");
				}
				else {
					System.out.print("0 ");
				}
			}
			
			if(isError(t)) {
				System.out.println("x");	
			}
			else {
				System.out.println(".");
			}
			
		}
	
		System.out.println("Number of probes: " + probes.size() + " number of transactions: " + transactions.size() );
	}

	@Override
	public BitSet getTransactionActivity(int t) {
		return transactions.get(t).activity;
	}
	
	@Override
	public String getTransactionName(int t) {
		return transactions.get(t).name;
	}
	
	@Override
	public Node getNodeOfProbe(int probeId) {
		int nodeId = probes.get(probeId);
		return tree.getNode(nodeId);
	}
	
}
