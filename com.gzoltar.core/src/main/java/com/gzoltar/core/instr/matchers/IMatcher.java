/**
 * Copyright (C) 2020 GZoltar contributors.
 * 
 * This file is part of GZoltar.
 * 
 * GZoltar is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * GZoltar is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with GZoltar. If
 * not, see <https://www.gnu.org/licenses/>.
 */
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
