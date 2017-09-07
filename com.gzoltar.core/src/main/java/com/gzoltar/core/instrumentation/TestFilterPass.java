package com.gzoltar.core.instrumentation;

import com.gzoltar.core.instrumentation.matchers.BlackList;
import com.gzoltar.core.instrumentation.matchers.MethodAnnotationMatcher;
import com.gzoltar.core.instrumentation.matchers.OrMatcher;
import com.gzoltar.core.instrumentation.matchers.SuperclassMatcher;

public class TestFilterPass extends FilterPass {

  public TestFilterPass() {
    BlackList junit3 = new BlackList(new SuperclassMatcher("junit.framework.TestCase"));
    BlackList junit4 = new BlackList(new OrMatcher(new MethodAnnotationMatcher("org.junit.Test"),
        new MethodAnnotationMatcher("org.junit.experimental.theories.Theory")));

    add(junit3);
    add(junit4);
  }
}
