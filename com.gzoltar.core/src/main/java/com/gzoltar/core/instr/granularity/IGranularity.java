package com.gzoltar.core.instr.granularity;

import com.gzoltar.core.model.Node;
import javassist.CtBehavior;
import javassist.CtClass;

public interface IGranularity {

  public boolean instrumentAtIndex(final int index, final int instrumentationSize);

  public boolean stopInstrumenting();

  public Node getNode(final CtClass ctClass, final CtBehavior ctBehavior, final int line);

}
