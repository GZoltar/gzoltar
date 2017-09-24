package com.gzoltar.core.instr.actions;

import com.gzoltar.core.instr.Outcome;
import com.gzoltar.core.instr.matchers.IMatcher;

public class BlackList extends AbstractAction {

  public BlackList(final IMatcher matcher) {
    super(matcher);
  }

  @Override
  public Outcome getAction(final boolean matches) {
    if (matches) {
      return Outcome.REJECT;
    }
    return Outcome.NEXT;
  }

}
