package com.gzoltar.core.instr.pass;

import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.instr.actions.BlackList;
import com.gzoltar.core.instr.actions.IActionTaker;
import com.gzoltar.core.instr.actions.WhiteList;
import com.gzoltar.core.instr.granularity.GranularityFactory;
import com.gzoltar.core.instr.granularity.IGranularity;
import com.gzoltar.core.instr.matchers.MethodModifierMatcher;
import com.gzoltar.core.model.Node;
import com.gzoltar.core.runtime.Collector;
import com.gzoltar.core.runtime.ProbeGroup.HitProbe;
import com.gzoltar.core.util.VMUtils;
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

public class InstrumentationPass implements IPass {

  private static final String HIT_VECTOR_TYPE = "[Z";

  public static final String HIT_VECTOR_NAME = "$__GZ_HIT_VECTOR__";

  private final AgentConfigs agentConfigs;

  private final FilterPass filter;

  public InstrumentationPass(final AgentConfigs agentConfigs) {
    this.agentConfigs = agentConfigs;

    IActionTaker includePublicMethods;
    if (this.agentConfigs.getInclPublicMethods()) {
      includePublicMethods = new WhiteList(new MethodModifierMatcher(Modifier.PUBLIC));
    } else {
      includePublicMethods = new BlackList(new MethodModifierMatcher(Modifier.PUBLIC));
    }

    this.filter = new FilterPass(includePublicMethods);
  }

  @Override
  public Outcome transform(final CtClass c) throws Exception {
    boolean instrumented = false;

    for (CtBehavior b : c.getDeclaredBehaviors()) {
      boolean behaviorInstrumented =
          this.transform(c, b).equals(IPass.Outcome.CANCEL) ? false : true;
      instrumented = instrumented || behaviorInstrumented;
    }

    if (instrumented) {
      // make field
      CtField f = CtField.make("private static boolean[] " + HIT_VECTOR_NAME + ";", c);
      f.setModifiers(f.getModifiers() | AccessFlag.SYNTHETIC);
      c.addField(f);

      // make class initializer
      CtConstructor initializer = c.makeClassInitializer();
      initializer.insertBefore(
          HIT_VECTOR_NAME + " = Collector.instance().getHitVector(\"" + c.getName() + "\");");
    }

    return Outcome.CONTINUE;
  }

  @Override
  public Outcome transform(final CtClass c, final CtBehavior b) throws Exception {
    IPass.Outcome instrumented = IPass.Outcome.CANCEL;

    // check whether this method should be instrumented
    if (this.filter.transform(c, b) == IPass.Outcome.CANCEL ||
        new SyntheticPass().transform(c, b) == IPass.Outcome.CANCEL ||
        new EnumPass(VMUtils.toVMName(c.getName())).transform(c, b) == IPass.Outcome.CANCEL) {
      return instrumented;
    }

    MethodInfo info = b.getMethodInfo();
    CodeAttribute ca = info.getCodeAttribute();

    if (ca == null) {
      // do not instrument methods with no code
      return instrumented;
    }

    CodeIterator ci = ca.iterator();

    if (b instanceof CtConstructor) {
      if (((CtConstructor) b).isClassInitializer()) {
        return instrumented;
      }
      ci.skipConstructor();
    }

    IGranularity g =
        GranularityFactory.getGranularity(c, info, ci, this.agentConfigs.getGranularity());

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

        instrumented = IPass.Outcome.CONTINUE;
      }

      if (g.stopInstrumenting()) {
        break;
      }
    }

    return instrumented;
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
