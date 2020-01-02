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

public abstract class AbstractAttributeMatcher implements IMatcher {

  private final String attribute;

  protected AbstractAttributeMatcher(final String attribute) {
    this.attribute = attribute;
  }

  @Override
  public boolean matches(final CtClass ctClass) {
    return ctClass.getAttribute(this.attribute) != null;
  }

  @Override
  public boolean matches(final CtBehavior ctBehavior) {
    return ctBehavior.getAttribute(this.attribute) != null;
  }

  @Override
  public boolean matches(final CtField ctField) {
    return ctField.getAttribute(this.attribute) != null;
  }

}
