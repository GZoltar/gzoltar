package com.gzoltar.core.instr.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.gzoltar.core.instr.Outcome;
import com.gzoltar.core.instr.actions.IAction;
import javassist.CtBehavior;
import javassist.CtClass;

public class Filter implements IFilter {

  private final List<IAction> actions = new ArrayList<IAction>();

  public Filter(final IAction... actions) {
    this.actions.addAll(Arrays.asList(actions));
  }

  public void add(final IAction action) {
    this.actions.add(action);
  }

  @Override
  public Outcome filter(final CtClass ctClass) {
    return this.filter(ctClass, this.actions);
  }

  @Override
  public Outcome filter(final CtBehavior ctBehavior) {
    return this.filter(ctBehavior, this.actions);
  }

  protected Outcome filter(final Object object, final List<IAction> actions) {
    for (IAction action : actions) {
      switch (this.filter(object, action)) {
        case ACCEPT:
          return Outcome.NEXT;
        case NEXT:
        default:
          continue;
        case REJECT:
          return Outcome.REJECT;
      }
    }

    return Outcome.NEXT;
  }

  protected Outcome filter(final Object object, final IAction action) {
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
