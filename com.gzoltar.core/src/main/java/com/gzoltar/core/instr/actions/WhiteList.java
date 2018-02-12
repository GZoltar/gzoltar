package com.gzoltar.core.instr.actions;

import com.gzoltar.core.instr.Outcome;
import com.gzoltar.core.instr.matchers.IMatcher;

public class WhiteList extends AbstractAction {

  public WhiteList(final IMatcher matcher) {
    super(matcher);
  }

  @Override
  public Outcome getAction(final boolean matches) {
    if (matches) {
      return Outcome.ACCEPT;
    }
    return Outcome.REJECT;
  }

}
