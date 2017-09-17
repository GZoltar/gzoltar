package com.gzoltar.core.instr.actions;

import com.gzoltar.core.instr.matchers.IMatcher;

public class WhiteList extends AbstractAction {

  public WhiteList(final IMatcher matcher) {
    super(matcher);
  }

  @Override
  public final Action getAction(boolean matches) {
    if (matches) {
      return Action.ACCEPT;
    }
    return Action.NEXT;
  }
}
