package com.gzoltar.core.instrumentation.matchers;

import java.security.ProtectionDomain;

import javassist.CtClass;
import javassist.CtMethod;

public class NotMatcher implements Matcher {
    private final Matcher matcher;
    public NotMatcher (Matcher matcher) {
        this.matcher = matcher;
    }

    @Override
    public final boolean matches (CtClass c, ProtectionDomain d) {
        return !matcher.matches(c, d);
    }

    @Override
    public final boolean matches (CtClass c, CtMethod m) {
        return !matcher.matches(c,m);
    }
}
