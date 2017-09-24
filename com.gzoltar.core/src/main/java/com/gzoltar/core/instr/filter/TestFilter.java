package com.gzoltar.core.instr.filter;

import com.gzoltar.core.instr.actions.BlackList;
import com.gzoltar.core.instr.matchers.MethodAnnotationMatcher;
import com.gzoltar.core.instr.matchers.OrMatcher;
import com.gzoltar.core.instr.matchers.SuperclassMatcher;

public final class TestFilter extends Filter {

  public TestFilter() {
    BlackList junit3 = new BlackList(new SuperclassMatcher("junit.framework.TestCase"));
    BlackList junit4 = new BlackList(new OrMatcher(new MethodAnnotationMatcher("org.junit.Test"),
        new MethodAnnotationMatcher("org.junit.experimental.theories.Theory")));

    this.add(junit3);
    this.add(junit4);
  }

}
