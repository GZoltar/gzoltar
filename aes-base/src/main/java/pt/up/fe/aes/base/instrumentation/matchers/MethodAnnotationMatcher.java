package pt.up.fe.aes.base.instrumentation.matchers;

import java.security.ProtectionDomain;

import javassist.CtClass;
import javassist.CtMethod;


public class MethodAnnotationMatcher implements Matcher {
    private final String annotation;

    public MethodAnnotationMatcher (String annotation) {
        this.annotation = annotation;
    }

    @Override
    public final boolean matches (CtClass c, ProtectionDomain d) {
        return false;
    }

    @Override
    public final boolean matches (CtClass c,
                                  CtMethod m) {
        try {
            return m.hasAnnotation(Class.forName(annotation));
        }
        catch (Exception e) {
            return false;
        }
    }
}