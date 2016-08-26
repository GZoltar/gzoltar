package pt.up.fe.ddu.base.instrumentation.matchers;

import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javassist.CtClass;
import javassist.CtMethod;


public class PrefixMatcher implements Matcher {
    private List<String> prefix = new LinkedList<String> ();

    public PrefixMatcher (String... strings) {
        prefix.addAll(Arrays.asList(strings));
    }

    public PrefixMatcher (List<String> strings) {
        prefix.addAll(strings);
    }

    @Override
    public final boolean matches (CtClass c, ProtectionDomain d) {
        return matches(c.getName());
    }

    @Override
    public final boolean matches (CtClass c, CtMethod m) {
        return matches(m.getName());
    }

    private boolean matches (String name) {
        for (String s : prefix) {
            if (name.startsWith(s))
                return true;
        }

        return false;
    }
}