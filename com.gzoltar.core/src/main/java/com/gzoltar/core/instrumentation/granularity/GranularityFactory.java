package com.gzoltar.core.instrumentation.granularity;

import javassist.CtClass;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.MethodInfo;

public class GranularityFactory {

	public static enum GranularityLevel {
		line,
		method,
		basicblock
	}

	public static Granularity getGranularity(CtClass c, MethodInfo mi, CodeIterator ci, GranularityLevel level) {
		switch(level) {
		case line:
			return new LineGranularity(c, mi, ci);
		case method:
			return new MethodGranularity(c, mi, ci);
		case basicblock:
		default:
			return new BasicBlockGranularity(c, mi, ci);
		}
	}
}
