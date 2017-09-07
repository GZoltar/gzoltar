package com.gzoltar.core.instrumentation.matchers;

import java.security.ProtectionDomain;
import javassist.CtClass;
import javassist.CtMethod;
import com.gzoltar.core.runtime.Collector;

public class DuplicateCollectorReferenceMatcher implements Matcher {

  @Override
  public boolean matches(CtClass c, ProtectionDomain d) {
    return Collector.instance().existsHitVector(c.getName());
  }

  @Override
  public boolean matches(CtClass c, CtMethod m) {
    return matches(c, (ProtectionDomain) null);
  }

}
