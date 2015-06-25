package pt.up.fe.aes.base.instrumentation.granularity;

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

}