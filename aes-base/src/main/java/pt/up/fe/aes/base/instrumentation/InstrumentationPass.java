package pt.up.fe.aes.base.instrumentation;

import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.bytecode.Bytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.Opcode;
import pt.up.fe.aes.base.instrumentation.granularity.Granularity;
import pt.up.fe.aes.base.instrumentation.granularity.GranularityFactory;
import pt.up.fe.aes.base.instrumentation.granularity.GranularityFactory.GranularityLevel;
import pt.up.fe.aes.base.model.Node;
import pt.up.fe.aes.base.runtime.Collector;
import pt.up.fe.aes.base.runtime.ProbeGroup.HitProbe;

public class InstrumentationPass implements Pass {

	private static final String HIT_VECTOR_TYPE = "[Z";
	public static final String HIT_VECTOR_NAME = "$__AES_HIT_VECTOR__";

	private final GranularityLevel granularity;

	public InstrumentationPass(GranularityLevel granularity) {
		this.granularity = granularity;
	}

	@Override
	public Outcome transform(CtClass c) throws Exception {

		for(CtBehavior b : c.getDeclaredBehaviors()) {
			handleBehavior(c, b);
		}

		//make field
		CtField f = CtField.make("private static boolean[] " + HIT_VECTOR_NAME + ";", c);
		c.addField(f);

		//make class initializer
		CtConstructor initializer = c.makeClassInitializer();
		initializer.insertBefore(HIT_VECTOR_NAME + " = Collector.instance().getHitVector(\"" + c.getName() + "\");");

		return Outcome.CONTINUE;
	}

	private void handleBehavior(CtClass c, CtBehavior b) throws Exception {
		MethodInfo info = b.getMethodInfo();
		CodeAttribute ca = info.getCodeAttribute();

		if(ca != null) {
			CodeIterator ci = ca.iterator();

			if (b instanceof CtConstructor) {
				if (((CtConstructor) b).isClassInitializer()) {
					return;
				}
				ci.skipConstructor();
			}

			Granularity g = GranularityFactory.getGranularity(c, info, ci, granularity);

			for(int instrSize = 0, index, curLine; ci.hasNext();) {
				index = ci.next();

				curLine = info.getLineNumber(index);

				if (curLine == -1)
					continue;

				if(g.instrumentAtIndex(index, instrSize)) {
					Node n = g.getNode(c, b, curLine);
					Bytecode bc = getInstrumentationCode(c, n, info.getConstPool());
					ci.insert(index, bc.get());
					instrSize += bc.length();
				}

				if(g.stopInstrumenting())
					break;
			}
		}
	}

	private Bytecode getInstrumentationCode(CtClass c, Node n, ConstPool constPool) {
		Bytecode b = new Bytecode(constPool);
		HitProbe p = getHitProbe(c, n);

		b.addGetstatic(c, HIT_VECTOR_NAME, HIT_VECTOR_TYPE);
		b.addIconst(p.getLocalId());
		b.addOpcode(Opcode.ICONST_1);
		b.addOpcode(Opcode.BASTORE);

		return b;
	}

	public HitProbe getHitProbe(CtClass cls, Node n) {
		Collector c = Collector.instance();
		
		return c.createHitProbe(cls.getName(), n.getId());
	}
}
