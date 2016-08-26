package pt.up.fe.ddu.base.instrumentation.matchers;

import java.security.ProtectionDomain;

import javassist.CtClass;
import javassist.CtMethod;

public interface ActionTaker {
    public static enum Action {
        ACCEPT, NEXT, REJECT
    }

    Action getAction (CtClass c, ProtectionDomain d);
    Action getAction (CtClass c, CtMethod m);
}