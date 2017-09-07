package com.gzoltar.core;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import javassist.ClassPool;
import javassist.CtClass;
import com.gzoltar.core.instrumentation.Pass;
import com.gzoltar.core.runtime.Collector;

public class Agent implements ClassFileTransformer {

  private static List<Pass> instrumentationPasses = new ArrayList<Pass>();

  public static void premain(String agentArgs, Instrumentation inst) {

    AgentConfigs agentConfigs = AgentConfigs.deserialize(agentArgs);

    Collector.start(agentConfigs.getEventListener());
    instrumentationPasses = agentConfigs.getInstrumentationPasses();

    Agent a = new Agent();
    inst.addTransformer(a);
  }

  public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
      ProtectionDomain protectionDomain, byte[] classfileBuffer)
      throws IllegalClassFormatException {

    if (loader == null)
      return null;

    CtClass c = null;
    ClassPool cp = null;
    byte[] ret = null;

    try {
      cp = ClassPool.getDefault();
      c = cp.makeClass(new java.io.ByteArrayInputStream(classfileBuffer));
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }

    try {
      cp.importPackage("com.gzoltar.core.runtime");

      for (Pass p : instrumentationPasses) {
        switch (p.transform(c, protectionDomain)) {
          case CANCEL:
            c.detach();
            return null;

          case CONTINUE:
            continue;
          case FINISH:
          default:
            break;
        }
      }

      ret = c.toBytecode();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }

    c.detach();
    return ret;
  }

}
