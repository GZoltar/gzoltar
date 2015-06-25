package pt.up.fe.aes.base.instrumentation.granularity;

import javassist.CtClass;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.MethodInfo;

public abstract class AbstractGranularity implements Granularity {
	
	protected CtClass c;
	protected MethodInfo mi;
	protected CodeIterator ci;

	public AbstractGranularity(CtClass c, MethodInfo mi, CodeIterator ci) {
		this.c = c;
		this.mi = mi;
		this.ci = ci;
	}
}