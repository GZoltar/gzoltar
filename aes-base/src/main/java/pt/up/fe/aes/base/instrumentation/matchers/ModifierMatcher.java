package pt.up.fe.aes.base.instrumentation.matchers;

import javassist.CtClass;
import javassist.CtMethod;


public class ModifierMatcher implements Matcher {
    private final int modifierMask;

    public ModifierMatcher (int modifierMask) {
        this.modifierMask = modifierMask;
    }

    @Override
    public final boolean matches (CtClass c) {
        return matches(c.getModifiers());
    }

    @Override
    public final boolean matches (CtClass c, CtMethod m) {
        return matches(m.getModifiers());
    }

    private boolean matches (int modifier) {
        return (modifier & modifierMask) != 0;
    }
}