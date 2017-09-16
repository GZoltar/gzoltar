package com.gzoltar.core.instr.actions;

import com.gzoltar.core.instr.matchers.IMatcher;

public class WhiteList extends AbstractActionTaker {

  public WhiteList(IMatcher m) {
    super(m);
  }

  @Override
  public final Action getAction(boolean matches) {
    if (matches) {
      return Action.ACCEPT;
    }
    return Action.NEXT;
  }
}
