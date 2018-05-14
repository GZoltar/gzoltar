package com.gzoltar.core.instr.filter;

import com.gzoltar.core.instr.actions.BlackList;
import com.gzoltar.core.instr.matchers.MethodEmptyMatcher;

/**
 * Filters all Java methods but constructors and static initialisers without any source code.
 */
public class EmptyMethodFilter extends Filter {

  public EmptyMethodFilter() {
    this.add(new BlackList(new MethodEmptyMatcher()));
  }

}
