package pt.up.fe.aes.base.instrumentation.matchers;

import javassist.CtClass;
import javassist.CtMethod;

public abstract class AbstractActionTaker implements ActionTaker {
    private final Matcher matcher;


    public AbstractActionTaker (Matcher matcher) {
        this.matcher = matcher;
    }

    @Override
    public final Action getAction (CtClass c) {
        return getAction(matcher.matches(c));
    }

    @Override
    public final Action getAction (CtClass c, CtMethod m) {
        return getAction(matcher.matches(c,m));
    }

    protected abstract Action getAction (boolean matches);
}