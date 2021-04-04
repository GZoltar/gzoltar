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
import com.gzoltar.core.instr.filter.EmptyMethodFilter;
import com.gzoltar.core.runtime.Collector;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtMethod;

public abstract class AbstractInitMethodPass implements IPass {

  private static final String METHOD_STR;

  protected static final String ARRAY_OBJECT_NAME = "$tmpGZoltarData";

  private final EmptyMethodFilter emptyMethodFilter = new EmptyMethodFilter();

  private String classHash = null;

  protected String collectorCall = null;

  static {
    METHOD_STR = 
        InstrumentationConstants.INIT_METHOD_DESC_HUMAN + InstrumentationConstants.INIT_METHOD_NAME_WITH_ARGS + " { "
              + "if (" + InstrumentationConstants.FIELD_NAME + " == " + InstrumentationConstants.FIELD_INIT_VALUE + ") { "
                + "Object[] " + ARRAY_OBJECT_NAME + " = new Object[] { \"%s\",\"%s\",\"%d\" }; "
                + "%s "
                + InstrumentationConstants.FIELD_NAME + " = (" + InstrumentationConstants.FIELD_DESC_HUMAN + ") " + ARRAY_OBJECT_NAME + "[0]; "
              + "}"
            + "}";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Outcome transform(CtClass ctClass) throws Exception {
    CtMethod gzoltarInit =
        CtMethod.make(String.format(METHOD_STR, this.classHash, ctClass.getName(),
            Collector.instance().getProbeGroupByHash(this.classHash).getNumberOfProbes(),
            this.collectorCall), ctClass);
    if (ctClass.isInterface()) {
      gzoltarInit.setModifiers(gzoltarInit.getModifiers() | InstrumentationConstants.INIT_METHOD_INTF_ACC);
    } else {
      gzoltarInit.setModifiers(gzoltarInit.getModifiers() | InstrumentationConstants.INIT_METHOD_ACC);
    }
    ctClass.addMethod(gzoltarInit);
    return Outcome.ACCEPT;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Outcome transform(CtClass ctClass, CtBehavior ctBehavior) throws Exception {
    if (this.emptyMethodFilter.filter(ctBehavior) == Outcome.REJECT) {
      return Outcome.REJECT;
    }

    ctBehavior.insertBefore(
        InstrumentationConstants.INIT_METHOD_NAME_WITH_ARGS + InstrumentationConstants.EOL);
    return Outcome.ACCEPT;
  }

  /**
   * 
   * @param hash
   */
  public void setHash(String hash) {
    this.classHash = hash;
  }

}
