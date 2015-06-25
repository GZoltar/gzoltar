package pt.up.fe.aes.base.spectrum;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SpectrumFilter implements Spectrum {

	private List<Integer> componentsFilter;
	private List<Integer> transactionsFilter;
	private Spectrum delegate;
	
	public SpectrumFilter(Spectrum spectrum) {
		this.delegate = spectrum;
		
		this.componentsFilter = new LinkedList<Integer>();
		for(int c = 0; c < spectrum.getComponentsSize(); c++) {
			this.componentsFilter.add(c);
		}
		
		this.transactionsFilter = new LinkedList<Integer>();
		for(int t = 0; t < spectrum.getTransactionsSize(); t++) {
			this.transactionsFilter.add(t);
		}
	}
	
	private SpectrumFilter(SpectrumFilter spectrumFilter) {
		this.delegate = spectrumFilter.delegate;
		this.componentsFilter = new LinkedList<Integer>(spectrumFilter.componentsFilter);
		this.transactionsFilter = new LinkedList<Integer>(spectrumFilter.transactionsFilter);
	}
	
	public SpectrumFilter copy() {
		return new SpectrumFilter(this);
	}
	
	public void stripComponent(int c) {
		Iterator<Integer> i = this.transactionsFilter.iterator();
		while(i.hasNext()) {
			int t = i.next();
			
			if (this.delegate.isInvolved(t,c)) {
				i.remove();
			}
		}
		
		filterComponent(c);
	}
	
	public void filterComponent(int c) {
		this.componentsFilter.remove((Integer) c);
	}
	
	public void filterTransaction(int t) {
		this.transactionsFilter.remove((Integer) t);
	}
	
	public void filterPassingTransactions() {
		Iterator<Integer> i = this.transactionsFilter.iterator();
		while(i.hasNext()) {
			int t = i.next();
			
			if(!this.delegate.isError(t)) {
				i.remove();
			}
		}
	}
	
	public boolean hasFailingTransactions() {
		Iterator<Integer> i = this.transactionsFilter.iterator();
		while(i.hasNext()) {
			int t = i.next();
			
			if(this.delegate.isError(t)) {
				return true;
			}
		}
		return false;
	}
	
	public int getComponent(int c) {
		return this.componentsFilter.get(c);
	}
	
	// Spectra Interface
	@Override
	public int getComponentsSize() {
		return this.componentsFilter.size();
	}
	
	@Override
	public int getTransactionsSize() {
		return this.transactionsFilter.size();
	}

	@Override
	public boolean isInvolved(int t, int c) {
		return delegate.isInvolved(this.transactionsFilter.get(t), 
								   this.componentsFilter.get(c));
	}

	@Override
	public boolean isError(int t) {
		return delegate.isError(this.transactionsFilter.get(t));
	}

	@Override
	public void print() {
		
	}
}
