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
package com.gzoltar.core.instr.actions;

import com.gzoltar.core.instr.Outcome;
import com.gzoltar.core.instr.matchers.IMatcher;
import javassist.CtBehavior;
import javassist.CtClass;

public abstract class AbstractAction implements IAction {

  private final IMatcher matcher;

  public AbstractAction(final IMatcher matcher) {
    this.matcher = matcher;
  }

  @Override
  public Outcome getAction(final CtClass c) {
    return this.getAction(this.matcher.matches(c));
  }

  @Override
  public Outcome getAction(final CtBehavior b) {
    return this.getAction(this.matcher.matches(b));
  }

  protected abstract Outcome getAction(final boolean matches);

}
