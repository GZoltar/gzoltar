package com.gzoltar.core.instr.pass;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.instr.Outcome;
import com.gzoltar.core.instr.actions.BlackList;
import com.gzoltar.core.instr.filter.EnumFilter;
import com.gzoltar.core.instr.filter.Filter;
import com.gzoltar.core.instr.filter.IFilter;
import com.gzoltar.core.instr.filter.SyntheticFilter;
import com.gzoltar.core.instr.granularity.GranularityFactory;
import com.gzoltar.core.instr.granularity.GranularityLevel;
import com.gzoltar.core.instr.granularity.IGranularity;
import com.gzoltar.core.instr.matchers.MethodAnnotationMatcher;
import com.gzoltar.core.instr.matchers.MethodModifierMatcher;
import com.gzoltar.core.instr.matchers.MethodNameMatcher;
import com.gzoltar.core.model.Node;
import com.gzoltar.core.runtime.Collector;
import com.gzoltar.core.runtime.Probe;
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

  private final StackSizePass stackSizePass = new StackSizePass();

  private final List<IFilter> filters = new ArrayList<IFilter>();

  private final GranularityLevel granularity;

  private final Set<Integer> uniqueLineNumbers = new LinkedHashSet<Integer>();

  public InstrumentationPass(final AgentConfigs agentConfigs) {
    this.granularity = agentConfigs.getGranularity();

    // filter classes/methods according to users preferences

    if (!agentConfigs.getInclPublicMethods()) {
      this.filters.add(new Filter(new BlackList(new MethodModifierMatcher(Modifier.PUBLIC))));
    }

    if (!agentConfigs.getInclStaticConstructors()) {
      this.filters.add(new Filter(new BlackList(new MethodNameMatcher("<clinit>*"))));
    }

    if (!agentConfigs.getInclDeprecatedMethods()) {
      this.filters.add(new Filter(new BlackList(new MethodAnnotationMatcher(Deprecated.class.getCanonicalName()))));
    }

    // exclude synthetic methods
    this.filters.add(new SyntheticFilter());

    // exclude methods 'values' and 'valuesOf' of enum classes
    this.filters.add(new EnumFilter());
  }

  @Override
  public Outcome transform(final CtClass ctClass) throws Exception {
    boolean instrumented = false;

    // as the constructor of this class is only called once, the set of unique line numbers is only
    // initialised once. and as the instrumentation of previous classes could have populated this
    // set of unique line numbers, we must restart it
    this.uniqueLineNumbers.clear();

    for (CtBehavior ctBehavior : ctClass.getDeclaredBehaviors()) {
      boolean behaviorInstrumented =
          this.transform(ctClass, ctBehavior).equals(Outcome.REJECT) ? false : true;
      instrumented = instrumented || behaviorInstrumented;

      if (behaviorInstrumented) {
        // update stack size
        this.stackSizePass.transform(ctClass, ctBehavior);
      }
    }

    if (instrumented) {
      // make field
      CtField f = CtField.make("private static boolean[] " + HIT_VECTOR_NAME + ";", ctClass);
      f.setModifiers(f.getModifiers() | AccessFlag.SYNTHETIC);
      ctClass.addField(f);

      // make class initializer
      CtConstructor initializer = ctClass.makeClassInitializer();
      initializer.insertBefore(HIT_VECTOR_NAME + " = " + Collector.class.getName()
          + ".instance().getHitArray(\"" + ctClass.getName() + "\");");
    }

    return Outcome.ACCEPT;
  }

  @Override
  public Outcome transform(final CtClass ctClass, final CtBehavior ctBehavior) throws Exception {
    Outcome instrumented = Outcome.REJECT;

    // check whether this method should be instrumented
    for (IFilter filter : this.filters) {
      switch (filter.filter(ctBehavior)) {
        case REJECT:
          return instrumented;
        case ACCEPT:
        default:
          continue;
      }
    }

    MethodInfo info = ctBehavior.getMethodInfo();
    CodeAttribute ca = info.getCodeAttribute();

    if (ca == null) {
      // do not instrument methods with no code
      return instrumented;
    }

    CodeIterator ci = ca.iterator();
    IGranularity g = GranularityFactory.getGranularity(ctClass, info, this.granularity);

    for (int instrSize = 0, index, curLine; ci.hasNext(); this.uniqueLineNumbers.add(curLine)) {
      index = ci.next();

      curLine = info.getLineNumber(index);

      if (curLine == -1 || this.uniqueLineNumbers.contains(curLine)) {
        continue;
      } else if (ctBehavior instanceof CtConstructor) {
        if (((CtConstructor) ctBehavior).isConstructor() && curLine == 1) {
          continue;
        }
      }

      if (g.instrumentAtIndex(index, instrSize)) {
        Node n = g.getNode(ctClass, ctBehavior, curLine);
        Bytecode bc = this.getInstrumentationCode(ctClass, n, info.getConstPool());
        ci.insert(index, bc.get());
        instrSize += bc.length();

        instrumented = Outcome.ACCEPT;
      }

      if (g.stopInstrumenting()) {
        break;
      }
    }

    return instrumented;
  }

  private Bytecode getInstrumentationCode(CtClass ctClass, Node node, ConstPool constPool) {
    Bytecode b = new Bytecode(constPool);
    Probe p = this.getProbe(ctClass, node);

    b.addGetstatic(ctClass, HIT_VECTOR_NAME, HIT_VECTOR_TYPE);
    b.addIconst(p.getArrayIndex());
    b.addOpcode(Opcode.ICONST_1);
    b.addOpcode(Opcode.BASTORE);

    return b;
  }

  public Probe getProbe(CtClass ctClass, Node node) {
    Collector c = Collector.instance();
    return c.regiterProbe(ctClass.getName(), node);
  }

}
