package com.gzoltar.core.instr.pass;

import com.gzoltar.core.instr.Outcome;
import com.gzoltar.core.instr.filter.DuplicateCollectorReferenceFilter;
import javassist.CtBehavior;
import javassist.CtClass;

public class DuplicateCollectorReferencePass implements IPass {

  private final DuplicateCollectorReferenceFilter duplicateCollectorReferenceFilter =
      new DuplicateCollectorReferenceFilter();

  @Override
  public Outcome transform(CtClass ctClass) throws Exception {
    return this.duplicateCollectorReferenceFilter.filter(ctClass);
  }

  @Override
  public Outcome transform(CtClass ctClass, CtBehavior ctBehavior) throws Exception {
    return this.duplicateCollectorReferenceFilter.filter(ctBehavior);
  }

}
