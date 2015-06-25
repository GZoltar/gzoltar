package pt.up.fe.aes.base.instrumentation.granularity;

public interface Granularity {
	
	public abstract boolean instrumentAtIndex(int index, int instrumentationSize);

	public abstract boolean stopInstrumenting();

}