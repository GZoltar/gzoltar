package com.gzoltar.core.instr.actions;

import com.gzoltar.core.instr.matchers.IMatcher;

public class BlackList extends AbstractActionTaker {

  public BlackList(IMatcher m) {
    super(m);
  }

  @Override
  public final Action getAction(boolean matches) {
    if (matches) {
      return Action.REJECT;
    }
    return Action.NEXT;
  }
}
