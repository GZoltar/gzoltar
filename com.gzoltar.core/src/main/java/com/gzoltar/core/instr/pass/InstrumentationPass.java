package com.gzoltar.core.instr.pass;

import com.gzoltar.core.instr.granularity.Granularity;
import com.gzoltar.core.instr.granularity.GranularityFactory;
import com.gzoltar.core.instr.granularity.GranularityFactory.GranularityLevel;
import com.gzoltar.core.model.Node;
import com.gzoltar.core.runtime.Collector;
import com.gzoltar.core.runtime.ProbeGroup.HitProbe;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.Modifier;
import javassist.bytecode.AccessFlag;
import javassist.bytecode.Bytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.Opcode;

public class InstrumentationPass implements Pass {

  private static final String HIT_VECTOR_TYPE = "[Z";
  public static final String HIT_VECTOR_NAME = "$__GZ_HIT_VECTOR__";

  private final GranularityLevel granularity;
  private final boolean filterPublicModifier;

  public InstrumentationPass(GranularityLevel granularity, boolean filterPublicModifier) {
    this.granularity = granularity;
    this.filterPublicModifier = filterPublicModifier;
  }

  @Override
  public Outcome transform(final CtClass c) throws Exception {
    for (CtBehavior b : c.getDeclaredBehaviors()) {
      this.handleBehavior(c, b);
    }

    // make field
    CtField f = CtField.make("private static boolean[] " + HIT_VECTOR_NAME + ";", c);
    f.setModifiers(f.getModifiers() | AccessFlag.SYNTHETIC);
    c.addField(f);

    // make class initializer
    CtConstructor initializer = c.makeClassInitializer();
    initializer.insertBefore(
        HIT_VECTOR_NAME + " = Collector.instance().getHitVector(\"" + c.getName() + "\");");

    return Outcome.CONTINUE;
  }

  private void handleBehavior(CtClass c, CtBehavior b) throws Exception {
    MethodInfo info = b.getMethodInfo();
    CodeAttribute ca = info.getCodeAttribute();

    if (filterPublicModifier && !Modifier.isPublic(b.getModifiers())) {
      return;
    }

    if (ca != null) {
      CodeIterator ci = ca.iterator();

      if (b instanceof CtConstructor) {
        if (((CtConstructor) b).isClassInitializer()) {
          return;
        }
        ci.skipConstructor();
      }

      Granularity g = GranularityFactory.getGranularity(c, info, ci, granularity);

      for (int instrSize = 0, index, curLine; ci.hasNext();) {
        index = ci.next();

        curLine = info.getLineNumber(index);

        if (curLine == -1) {
          continue;
        }

        if (g.instrumentAtIndex(index, instrSize)) {
          Node n = g.getNode(c, b, curLine);
          Bytecode bc = getInstrumentationCode(c, n, info.getConstPool());
          ci.insert(index, bc.get());
          instrSize += bc.length();
        }

        if (g.stopInstrumenting()) {
          break;
        }
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
