package com.gzoltar.core.instr.granularity;

import com.gzoltar.core.model.Node;
import javassist.CtBehavior;
import javassist.CtClass;

public interface IGranularity {

  /**
   * 
   * @param index
   * @param instrumentationSize
   * @return
   */
  public boolean instrumentAtIndex(final int index, final int instrumentationSize);

  /**
   * 
   * @return
   */
  public boolean stopInstrumenting();

  /**
   * 
   * @param ctClass
   * @param ctBehavior
   * @param lineNumber
   * @return
   */
  public Node createNode(final CtClass ctClass, final CtBehavior ctBehavior, final int lineNumber);
}
