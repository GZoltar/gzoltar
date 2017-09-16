package com.gzoltar.core.instr.granularity;

import javassist.CtBehavior;
import javassist.CtClass;
import com.gzoltar.core.model.Node;

public interface Granularity {

  public boolean instrumentAtIndex(int index, int instrumentationSize);

  public boolean stopInstrumenting();

  public Node getNode(CtClass cls, CtBehavior m, int line);
}
