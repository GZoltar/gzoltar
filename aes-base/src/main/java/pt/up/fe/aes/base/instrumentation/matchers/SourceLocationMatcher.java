package pt.up.fe.aes.base.instrumentation.matchers;

import java.security.CodeSource;
import java.security.ProtectionDomain;

import javassist.CtClass;
import javassist.CtMethod;

public class SourceLocationMatcher implements Matcher {

	private final String location;

	public SourceLocationMatcher(String location) {
		this.location = location.replace(" ", "%20");
	}

	@Override
	public boolean matches(CtClass c, ProtectionDomain d) {
		if (d != null) {
			CodeSource cs = d.getCodeSource();
			if (cs != null && cs.getLocation() != null) {
				return cs.getLocation().getPath().startsWith(location);
			}
		}

		return false;
	}

	@Override
	public boolean matches(CtClass c, CtMethod m) {
		return false;
	}

}
