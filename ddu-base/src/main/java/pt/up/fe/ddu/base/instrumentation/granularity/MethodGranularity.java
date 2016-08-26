package pt.up.fe.ddu.base.instrumentation.granularity;

import javassist.CtBehavior;
import javassist.CtClass;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.MethodInfo;
import pt.up.fe.ddu.base.model.Node;

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