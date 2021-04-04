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
package com.gzoltar.core.instr.pass;

import com.gzoltar.core.instr.InstrumentationConstants;
import com.gzoltar.core.instr.Outcome;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;

public class FieldPass implements IPass {

  private static final String fieldStr =
      InstrumentationConstants.FIELD_DESC_HUMAN + InstrumentationConstants.FIELD_NAME + " = "
          + InstrumentationConstants.FIELD_INIT_VALUE + InstrumentationConstants.EOL;

  @Override
  public Outcome transform(CtClass ctClass) throws Exception {
    CtField f = CtField.make(fieldStr, ctClass);
    if (ctClass.isInterface()) {
      f.setModifiers(f.getModifiers() | InstrumentationConstants.FIELD_INTF_ACC);
    } else {
      f.setModifiers(f.getModifiers() | InstrumentationConstants.FIELD_ACC);
    }
    ctClass.addField(f);

    return Outcome.ACCEPT;
  }

  @Override
  public Outcome transform(CtClass ctClass, CtBehavior ctBehavior) throws Exception {
    return Outcome.REJECT;
  }

}
