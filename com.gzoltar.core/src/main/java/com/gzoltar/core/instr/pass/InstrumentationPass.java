package com.gzoltar.core.instr.pass;

import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.instr.actions.BlackList;
import com.gzoltar.core.instr.actions.IActionTaker;
import com.gzoltar.core.instr.actions.WhiteList;
import com.gzoltar.core.instr.granularity.GranularityFactory;
import com.gzoltar.core.instr.granularity.IGranularity;
import com.gzoltar.core.instr.matchers.MethodAttributeMatcher;
import com.gzoltar.core.instr.matchers.MethodModifierMatcher;
import com.gzoltar.core.instr.matchers.OrMatcher;
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
import javassist.bytecode.SyntheticAttribute;

public class InstrumentationPass implements IPass {

  private static final String HIT_VECTOR_TYPE = "[Z";

  public static final String HIT_VECTOR_NAME = "$__GZ_HIT_VECTOR__";

  private final AgentConfigs agentConfigs;

  private final FilterPass filter;

  public InstrumentationPass(final AgentConfigs agentConfigs) {
    this.agentConfigs = agentConfigs;

    /**
     * Suppose we compile the following snippet of code with a JDK version < 5.
     * 
     * public class Main {
     *   public static void main(String[] args) {
     *     // print Class object of Main class
     *     System.out.println(Main.class);
     *   }
     * }
     * 
     * As javac of, for example, JDK-4 does not support 'class literals', it creates a new static
     * method called 'class$' which returns a java.lang.Class<?>. that method will be called when
     * 'Main.class' code is executed. However, that method does not exist in the source-code, it
     * only exists in bytecode. As GZoltar relies on bytecode it assumes that method 'class$' is to
     * instrument as any other method. With Java-5 a new variant on ldc_w has been defined to
     * implement class literals, and therefore this is not a problem anymore.
     * 
     * Check out https://blogs.oracle.com/sundararajan/entry/class_literals_in_jdk_1 for more
     * information.
     * 
     * 
     * Another example of synthetic methods are the methods called "access$[0-9]*" which allow class
     * access to private fields from anonymous inner classes. Example:
     * 
     * class Access {
     *   private String y = "y";
     *   public static void x() {
     *      final access a = new Access();
     *      Object o = new Object() {
     *        public String toString() {
     *            return a.y;
     *        }
     *      };
     *   }
     * }
     * 
     * The anonymous Object subclass only has private access in code here. The compiler generates a
     * static method in Access which simply returns the field from the passed in object. The
     * compiler also replaces the call to "a.y" with "access$000(a)".
     */
    BlackList excludeBridgeSyntheticMethods =
        new BlackList(new OrMatcher(new MethodModifierMatcher(AccessFlag.BRIDGE),
            new MethodModifierMatcher(AccessFlag.SYNTHETIC),
            new MethodAttributeMatcher(SyntheticAttribute.tag)));

    IActionTaker includePublicMethods;
    if (this.agentConfigs.getInclPublicMethods()) {
      includePublicMethods = new WhiteList(new MethodModifierMatcher(Modifier.PUBLIC));
    } else {
      includePublicMethods = new BlackList(new MethodModifierMatcher(Modifier.PUBLIC));
    }

    this.filter = new FilterPass(excludeBridgeSyntheticMethods, includePublicMethods);
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
