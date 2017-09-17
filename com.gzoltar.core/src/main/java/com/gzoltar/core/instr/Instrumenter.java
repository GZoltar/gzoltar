package com.gzoltar.core.instr;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import com.gzoltar.core.instr.granularity.GranularityFactory.GranularityLevel;
import com.gzoltar.core.instr.pass.InstrumentationPass;
import com.gzoltar.core.instr.pass.IPass;
import com.gzoltar.core.instr.pass.StackSizePass;
import javassist.ClassPool;
import javassist.CtClass;

public class Instrumenter {

  private final GranularityLevel granularity;

  public Instrumenter(GranularityLevel granularity) {
    this.granularity = granularity;
  }

  public byte[] instrument(final byte[] classfileBuffer) throws Exception {
    ClassPool cp = ClassPool.getDefault();
    CtClass cc = cp.makeClass(new ByteArrayInputStream(classfileBuffer));
    return this.instrument(cc);
  }

  public byte[] instrument(final CtClass cc) throws Exception {
    ClassPool cp = ClassPool.getDefault();
    cp.importPackage("com.gzoltar.core.runtime");

    List<IPass> instrumentationPasses = new ArrayList<IPass>();
    // instrumentationPasses.add(new TestFilterPass()); // do not instrument test classes/cases
    instrumentationPasses.add(new InstrumentationPass(this.granularity, false)); // TODO false?
    instrumentationPasses.add(new StackSizePass());

    for (IPass p : instrumentationPasses) {
      switch (p.transform(cc)) {
        case CANCEL:
          cc.detach();
          return null;
        case CONTINUE:
          continue;
        case FINISH:
        default:
          break;
      }
    }

    byte[] bytecode = cc.toBytecode();
    cc.detach();

    return bytecode;
  }

}
