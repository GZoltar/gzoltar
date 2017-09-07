package com.gzoltar.core.instrumentation.matchers;

public class BlackList extends AbstractActionTaker {
  public BlackList(Matcher m) {
    super(m);
  }

  @Override
  public final Action getAction(boolean matches) {
    if (matches)
      return Action.REJECT;

    return Action.NEXT;
  }
}
