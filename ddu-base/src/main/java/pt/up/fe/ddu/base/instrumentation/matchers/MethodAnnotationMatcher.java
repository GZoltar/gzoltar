package pt.up.fe.ddu.base.instrumentation.matchers;

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
    	for(CtMethod m : c.getDeclaredMethods()) {
    		if (matches(c, m)) {
    			return true;
    		}
    	}
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