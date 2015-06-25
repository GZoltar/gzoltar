package pt.up.fe.aes.base.spectrum;

public interface Spectrum {

	public int getComponentsSize();

	public int getTransactionsSize();

	public boolean isInvolved(int t, int c);

	public boolean isError(int t);

	public void print();
}
