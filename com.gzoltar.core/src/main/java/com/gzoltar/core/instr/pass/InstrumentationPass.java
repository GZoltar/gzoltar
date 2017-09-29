package com.gzoltar.core.instr.pass;

import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.instr.Outcome;
import com.gzoltar.core.instr.actions.BlackList;
import com.gzoltar.core.instr.actions.IAction;
import com.gzoltar.core.instr.actions.WhiteList;
import com.gzoltar.core.instr.filter.EnumFilter;
import com.gzoltar.core.instr.filter.Filter;
import com.gzoltar.core.instr.filter.IFilter;
import com.gzoltar.core.instr.filter.SyntheticFilter;
import com.gzoltar.core.instr.granularity.GranularityFactory;
import com.gzoltar.core.instr.granularity.GranularityLevel;
import com.gzoltar.core.instr.granularity.IGranularity;
import com.gzoltar.core.instr.matchers.MethodModifierMatcher;
import com.gzoltar.core.instr.matchers.MethodNameMatcher;
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

public class InstrumentationPass implements IPass {

  private static final String HIT_VECTOR_TYPE = "[Z";

  public static final String HIT_VECTOR_NAME = "$__GZ_HIT_VECTOR__";

  private final IFilter[] filters;

  private final GranularityLevel granularity;

  public InstrumentationPass(final AgentConfigs agentConfigs) {
    this.granularity = agentConfigs.getGranularity();

    IAction includePublicMethods;
    if (agentConfigs.getInclPublicMethods()) {
      includePublicMethods = new WhiteList(new MethodModifierMatcher(Modifier.PUBLIC));
    } else {
      includePublicMethods = new BlackList(new MethodModifierMatcher(Modifier.PUBLIC));
    }

    IAction includeStaticConstructors;
    if (agentConfigs.getInclStaticConstructors()) {
      includeStaticConstructors = new WhiteList(new MethodNameMatcher("<clinit>*"));
    } else {
      includeStaticConstructors = new BlackList(new MethodNameMatcher("<clinit>*"));
    }

    this.filters = new IFilter[] {
        // filter classes/methods according to users preferences
        new Filter(includePublicMethods),
        new Filter(includeStaticConstructors),
        // exclude synthetic methods
        new SyntheticFilter(),
        // exclude methods 'values' and 'valuesOf' of enum classes
        new EnumFilter()};
  }

  @Override
  public Outcome transform(final CtClass ctClass) throws Exception {
    boolean instrumented = false;

    for (CtBehavior ctBehavior : ctClass.getDeclaredBehaviors()) {
      boolean behaviorInstrumented =
          this.transform(ctClass, ctBehavior).equals(Outcome.REJECT) ? false : true;
      instrumented = instrumented || behaviorInstrumented;
    }

    if (instrumented) {
      // make field
      CtField f = CtField.make("private static boolean[] " + HIT_VECTOR_NAME + ";", ctClass);
      f.setModifiers(f.getModifiers() | AccessFlag.SYNTHETIC);
      ctClass.addField(f);

      // make class initializer
      CtConstructor initializer = ctClass.makeClassInitializer();
      initializer.insertBefore(HIT_VECTOR_NAME + " = " + Collector.class.getName()
          + ".instance().getHitVector(\"" + ctClass.getName() + "\");");
    }

    return Outcome.NEXT;
  }

  @Override
  public Outcome transform(final CtClass ctClass, final CtBehavior ctBehavior) throws Exception {
    Outcome instrumented = Outcome.REJECT;

    // check whether this method should be instrumented
    for (IFilter filter : this.filters) {
      switch (filter.filter(ctBehavior)) {
        case REJECT:
          return instrumented;
        case NEXT:
          continue;
        case ACCEPT:
        default:
          break;
      }
    }

    MethodInfo info = ctBehavior.getMethodInfo();
    CodeAttribute ca = info.getCodeAttribute();

    if (ca == null) {
      // do not instrument methods with no code
      return instrumented;
    }

    CodeIterator ci = ca.iterator();
    IGranularity g = GranularityFactory.getGranularity(ctClass, info, ci, this.granularity);

    for (int instrSize = 0, index, curLine; ci.hasNext();) {
      index = ci.next();

      curLine = info.getLineNumber(index);

      if (curLine == -1) {
        continue;
      }

      if (g.instrumentAtIndex(index, instrSize)) {
        Node n = g.getNode(ctClass, ctBehavior, curLine);
        Bytecode bc = this.getInstrumentationCode(ctClass, n, info.getConstPool());
        ci.insert(index, bc.get());
        instrSize += bc.length();

        instrumented = Outcome.NEXT;
      }

      if (g.stopInstrumenting()) {
        break;
      }
    }

    return instrumented;
  }

  private Bytecode getInstrumentationCode(CtClass ctClass, Node node, ConstPool constPool) {
    Bytecode b = new Bytecode(constPool);
    HitProbe p = this.getHitProbe(ctClass, node);

    b.addGetstatic(ctClass, HIT_VECTOR_NAME, HIT_VECTOR_TYPE);
    b.addIconst(p.getLocalId());
    b.addOpcode(Opcode.ICONST_1);
    b.addOpcode(Opcode.BASTORE);

    return b;
  }

  public HitProbe getHitProbe(CtClass ctClass, Node node) {
    Collector c = Collector.instance();
    return c.createHitProbe(ctClass.getName(), node.getId());
  }

}
