package pt.up.fe.ddu.base.instrumentation;

import pt.up.fe.ddu.base.instrumentation.matchers.BlackList;
import pt.up.fe.ddu.base.instrumentation.matchers.MethodAnnotationMatcher;
import pt.up.fe.ddu.base.instrumentation.matchers.OrMatcher;
import pt.up.fe.ddu.base.instrumentation.matchers.SuperclassMatcher;

public class TestFilterPass extends FilterPass {
	
	public TestFilterPass() {
		BlackList junit3 = new BlackList(new SuperclassMatcher("junit.framework.TestCase"));
		BlackList junit4 = new BlackList(
				new OrMatcher(new MethodAnnotationMatcher("org.junit.Test"),
						      new MethodAnnotationMatcher("org.junit.experimental.theories.Theory")));
		
		add(junit3);
		add(junit4);
	}
}
