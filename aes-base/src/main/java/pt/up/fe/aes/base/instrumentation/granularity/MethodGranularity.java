package pt.up.fe.aes.base.instrumentation.granularity;

import pt.up.fe.aes.base.model.Node;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.MethodInfo;

public class MethodGranularity extends AbstractGranularity {

	public MethodGranularity(CtClass c, MethodInfo mi, CodeIterator ci) {
		super(c, mi, ci);
	}

	@Override
	public boolean instrumentAtIndex(int index, int instrumentationSize) {
		return true;
	}

	@Override
	public boolean stopInstrumenting() {
		return true;
	}

	@Override
	public Node getNode(CtClass cls, CtBehavior m, int line) {
		return super.getNode(cls, m);
	}
}