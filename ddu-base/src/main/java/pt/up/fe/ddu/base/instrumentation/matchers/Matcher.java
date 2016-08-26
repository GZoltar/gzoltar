package pt.up.fe.ddu.base.instrumentation.matchers;

import java.security.ProtectionDomain;

import javassist.CtClass;
import javassist.CtMethod;


public interface Matcher {
    boolean matches (CtClass c, ProtectionDomain d);
    boolean matches (CtClass c, CtMethod m);
}