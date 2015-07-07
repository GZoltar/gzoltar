package pt.up.fe.aes.base.instrumentation.matchers;

import java.security.ProtectionDomain;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;


public class SuperclassMatcher implements Matcher {
    private final String superclass;

    public SuperclassMatcher (String superclass) {
        this.superclass = superclass;
    }

    @Override
    public final boolean matches (CtClass c, ProtectionDomain d) {
        return matchesSuperclass (c);
    }

    @Override
    public final boolean matches (CtClass c, CtMethod m) {
        return matchesSuperclass (c);
    }

    private boolean matchesSuperclass (CtClass c) {
    	try {
    		CtClass superCtClass = c.getSuperclass();
    		while(superCtClass != null) {
    			if(superclass.equals(superCtClass.getName()))
    				return true;
    			superCtClass = superCtClass.getSuperclass();
    		}
    		return false;
    	} catch (NotFoundException e) {
    		return false;
    	}
    }
}