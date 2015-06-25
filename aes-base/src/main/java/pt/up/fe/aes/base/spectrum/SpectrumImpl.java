package pt.up.fe.aes.base.spectrum;

import java.util.ArrayList;
import java.util.BitSet;

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
	}

	public void addTransaction(String transactionName, boolean[] activity, boolean isError) {
		transactions.add(new Transaction(transactionName, activity, isError));
	}
	
	public Tree getTree() {
		return tree;
	}

	public void addProbe(int id, int nodeId) {
		probes.ensureCapacity(id + 1);
		
		while(probes.size() <= id) {
			probes.add(null);
		}
		
		probes.set(id, nodeId);
	}

	public void print() {
		/*tree.print();
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
			
		}*/
		
		tree.print();
		System.out.println("Number of probes: " + probes.size() + " number of transactions: " + transactions.size() );
	}
}
