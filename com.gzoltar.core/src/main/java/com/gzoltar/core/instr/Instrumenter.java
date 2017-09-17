package com.gzoltar.core.instr;

import java.io.ByteArrayInputStream;
import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.instr.pass.IPass;
import com.gzoltar.core.instr.pass.InstrumentationPass;
import com.gzoltar.core.instr.pass.StackSizePass;
import javassist.ClassPool;
import javassist.CtClass;

public class Instrumenter {

  private final IPass[] passes;

  public Instrumenter(final AgentConfigs agentConfigs) {
    this.passes = new IPass[] {
        //new TestFilterPass(), // do not instrument test classes/cases
        new InstrumentationPass(agentConfigs),
        new StackSizePass()
    };
  }

  public byte[] instrument(final byte[] classfileBuffer) throws Exception {
    ClassPool cp = ClassPool.getDefault();
    CtClass cc = cp.makeClass(new ByteArrayInputStream(classfileBuffer));
    return this.instrument(cc);
  }

  public byte[] instrument(final CtClass cc) throws Exception {
    for (IPass p : this.passes) {
      switch (p.transform(cc)) {
        case REJECT:
          cc.detach();
          return null;
        case NEXT:
          continue;
        case ACCEPT:
        default:
          break;
      }
    }

    byte[] bytecode = cc.toBytecode();
    cc.detach();

    return bytecode;
  }

}
