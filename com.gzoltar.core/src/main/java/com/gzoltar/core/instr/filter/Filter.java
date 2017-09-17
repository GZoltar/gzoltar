package com.gzoltar.core.instr.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.gzoltar.core.instr.actions.Action;
import com.gzoltar.core.instr.actions.IAction;
import javassist.CtBehavior;
import javassist.CtClass;

public class Filter implements IFilter {

  private final List<IAction> actions = new ArrayList<IAction>();

  public Filter(IAction... actions) {
    this.actions.addAll(Arrays.asList(actions));
  }

  public void add(IAction action) {
    this.actions.add(action);
  }

  @Override
  public Action filter(final CtClass ctClass) {
    return this.filter(ctClass, this.actions);
  }

  @Override
  public Action filter(final CtBehavior ctBehavior) {
    return this.filter(ctBehavior, this.actions);
  }

  protected Action filter(final Object object, final List<IAction> actions) {
    for (IAction action : actions) {
      switch (filter(object, action)) {
        case ACCEPT:
          return Action.NEXT;
        case NEXT:
        default:
          continue;
        case REJECT:
          return Action.REJECT;
      }
    }

    return Action.NEXT;
  }

  protected Action filter(final Object object, final IAction action) {
    if (object instanceof CtClass) {
      return action.getAction((CtClass) object);
    } else if (object instanceof CtBehavior) {
      return action.getAction((CtBehavior) object);
    } else {
      throw new IllegalArgumentException(
          "Object of type " + object.getClass().getName() + " is not allowed");
    }
  }
}
