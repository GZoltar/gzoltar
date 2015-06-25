package pt.up.fe.aes.base.instrumentation.matchers;

import javassist.CtClass;
import javassist.CtMethod;

public class AndMatcher implements Matcher {
    private final Matcher[] matchers;

    public AndMatcher (Matcher... matchers) {
        this.matchers = matchers;
    }

    @Override
    public final boolean matches (CtClass c) {
        for (Matcher mat : matchers) {
            if (!mat.matches(c))
                return false;
        }

        return true;
    }

    @Override
    public final boolean matches (CtClass c,
                                  CtMethod m) {
        for (Matcher mat : matchers) {
            if (!mat.matches(c, m))
                return false;
        }

        return true;
    }
}