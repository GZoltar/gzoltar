package pt.up.fe.aes.base.instrumentation.matchers;

import javassist.CtClass;
import javassist.CtMethod;

public class NotMatcher implements Matcher {
    private final Matcher matcher;
    public NotMatcher (Matcher matcher) {
        this.matcher = matcher;
    }

    @Override
    public final boolean matches (CtClass c) {
        return !matcher.matches(c);
    }

    @Override
    public final boolean matches (CtClass c, CtMethod m) {
        return !matcher.matches(c,m);
    }
}