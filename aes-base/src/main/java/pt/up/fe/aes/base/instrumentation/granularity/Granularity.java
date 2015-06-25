package pt.up.fe.aes.base.instrumentation.granularity;

import javassist.CtBehavior;
import javassist.CtClass;
import pt.up.fe.aes.base.model.Node;

public interface Granularity {
	
	public boolean instrumentAtIndex(int index, int instrumentationSize);

	public boolean stopInstrumenting();
	
	public Node getNode(CtClass cls, CtBehavior m, int line);
}