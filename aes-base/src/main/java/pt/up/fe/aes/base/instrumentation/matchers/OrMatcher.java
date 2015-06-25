package pt.up.fe.aes.base.instrumentation.matchers;

import javassist.CtClass;
import javassist.CtMethod;

public class OrMatcher implements Matcher {
    private final Matcher[] matchers;

    public OrMatcher (Matcher... matchers) {
        this.matchers = matchers;
    }

    @Override
    public final boolean matches (CtClass c) {
        for (Matcher mat : matchers) {
            if (mat.matches(c))
                return true;
        }

        return false;
    }

    @Override
    public final boolean matches (CtClass c,
                                  CtMethod m) {
        for (Matcher mat : matchers) {
            if (mat.matches(c, m))
                return true;
        }

        return false;
    }
}