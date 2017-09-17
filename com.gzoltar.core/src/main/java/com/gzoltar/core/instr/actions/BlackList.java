package com.gzoltar.core.instr.actions;

import com.gzoltar.core.instr.matchers.IMatcher;

public class BlackList extends AbstractAction {

  public BlackList(final IMatcher matcher) {
    super(matcher);
  }

  @Override
  public final Action getAction(boolean matches) {
    if (matches) {
      return Action.REJECT;
    }
    return Action.NEXT;
  }
}
