package pt.up.fe.aes.base.instrumentation.granularity;

import javassist.CtClass;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.MethodInfo;

public class LineGranularity extends AbstractGranularity {

	private int currentLine = -1;

	public LineGranularity(CtClass c, MethodInfo mi, CodeIterator ci) {
		super(c, mi, ci);
	}

	@Override
	public boolean instrumentAtIndex(int index, int instrumentationSize) {
		int previousLine = currentLine;
		currentLine = mi.getLineNumber(index);
		System.out.println(c.getName() + " instrument at index " + previousLine + " " + currentLine);
		return currentLine != previousLine;
	}

	@Override
	public boolean stopInstrumenting() {
		return false;
	}	
}