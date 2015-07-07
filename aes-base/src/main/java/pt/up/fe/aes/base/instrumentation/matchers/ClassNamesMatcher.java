package pt.up.fe.aes.base.instrumentation.matchers;

import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javassist.CtClass;
import javassist.CtMethod;

public class ClassNamesMatcher implements Matcher {
    private List<String> names = new LinkedList<String> ();

    public ClassNamesMatcher (String... strings) {
        names.addAll(Arrays.asList(strings));
    }

    public ClassNamesMatcher (List<String> strings) {
        names.addAll(strings);
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
        for (String s : names) {
            if (name.equals(s))
                return true;
        }

        return false;
    }
}
