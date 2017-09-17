package com.gzoltar.core.instr.matchers;

import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;

public interface IMatcher {

  /**
   * Matches a class
   * 
   * @param ctClass class
   * @return <code>true</code> if it matches, <code>false</code> otherwise
   */
  public boolean matches(final CtClass ctClass);

  /**
   * Matches a method, a constructor, or a static constructor (class initializer)
   * 
   * @param ctBehavior a method, a constructor, or a static constructor (class initializer)
   * @return <code>true</code> if it matches, <code>false</code> otherwise
   */
  public boolean matches(final CtBehavior ctBehavior);

  /**
   * Matches a field
   * 
   * @param ctField a field
   * @return <code>true</code> if it matches, <code>false</code> otherwise
   */
  public boolean matches(final CtField ctField);

}
