package com.gzoltar.core.instr.filter;

import com.gzoltar.core.instr.actions.BlackList;
import com.gzoltar.core.instr.matchers.JUnitMatcher;

public final class TestFilter extends Filter {

  public TestFilter() {
    BlackList junit = new BlackList(new JUnitMatcher());
    this.add(junit);

    // TODO add support for TestNG
  }
}
