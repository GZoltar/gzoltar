package pt.up.fe.aes.base.instrumentation.granularity;

import javassist.CtClass;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.MethodInfo;

public class GranularityFactory {

	public static enum GranularityLevel {
		LINE,
		METHOD,
		BASIC_BLOCK
	}

	public static Granularity getGranularity(CtClass c, MethodInfo mi, CodeIterator ci, GranularityLevel level) {
		switch(level) {
		case LINE:
			return new LineGranularity(c, mi, ci);
		case METHOD:
			return new MethodGranularity(c, mi, ci);
		case BASIC_BLOCK:
		default:
			return new BasicBlockGranularity(c, mi, ci);
		}
	}
}
