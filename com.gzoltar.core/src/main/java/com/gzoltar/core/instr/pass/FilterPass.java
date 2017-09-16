package com.gzoltar.core.instr.pass;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import com.gzoltar.core.instr.actions.IActionTaker;
import javassist.CtClass;

public class FilterPass implements Pass {

  private final List<IActionTaker> actionTakers = new LinkedList<IActionTaker>();

  private Outcome fallbackOutcome = Outcome.CONTINUE;

  private Outcome acceptOutcome = Outcome.CONTINUE;

  private Outcome rejectOutcome = Outcome.CANCEL;

  public FilterPass(IActionTaker... actionTakers) {
    this.actionTakers.addAll(Arrays.asList(actionTakers));
  }

  public FilterPass(List<IActionTaker> actionTakers) {
    this.actionTakers.addAll(actionTakers);
  }

  public void add(IActionTaker actionTaker) {
    this.actionTakers.add(actionTaker);
  }

  public FilterPass setFallbackOutcome(Outcome targetOutcome) {
    this.fallbackOutcome = targetOutcome;
    return this;
  }

  public FilterPass setAcceptOutcome(Outcome targetOutcome) {
    this.acceptOutcome = targetOutcome;
    return this;
  }

  public FilterPass setRejectOutcome(Outcome targetOutcome) {
    this.rejectOutcome = targetOutcome;
    return this;
  }

  @Override
  public Outcome transform(final CtClass c) throws Exception {
    for (IActionTaker at : this.actionTakers) {
      IActionTaker.Action ret = at.getAction(c);
      switch (ret) {
        case ACCEPT:
          return this.acceptOutcome;
        case NEXT:
          continue;
        case REJECT:
          return this.rejectOutcome;
        default:
          continue;
      }
    }

    return this.fallbackOutcome;
  }

}
