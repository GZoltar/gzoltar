/**
 * Copyright (C) 2018 GZoltar contributors.
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
package com.gzoltar.core.instr;

import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.instr.matchers.ClassNoLineOfCodeMatcher;
import com.gzoltar.core.instr.pass.ClinitPass;
import com.gzoltar.core.instr.pass.IPass;
import com.gzoltar.core.instr.pass.PutGetStaticPass;
import com.gzoltar.core.instr.pass.ResetPass;
import javassist.CtClass;

/**
 * In order to reset all static fields of a Java class <code>C</code>, this class performs the
 * following instrumentation: 1) instrument all putstatic and getstatic calls by appending a call to
 * a custom resetter method; 2) creates a custom resetter method by clonning the existing static
 * constructor of a Java class <code>C</code>; 3) instrument all methods of <code>C</code> by
 * appending a calling to its custom resetter method.
 */
public class ResetterInstrumenter extends AbstractInstrumenter {

  private final InstrumentationLevel instrumentationLevel;

  private static final ClassNoLineOfCodeMatcher classNoLineOfCodeMatcher =
      new ClassNoLineOfCodeMatcher();

  public static final String RESETTER_CLASS_SUFFIX = "$$GZoltarResetter";
  /**
   * 
   * @param agentConfigs
   */
  public ResetterInstrumenter(final AgentConfigs agentConfigs) {
    super(new IPass[] {new PutGetStaticPass(agentConfigs.getInstrumentationLevel()),
        new ClinitPass(), new ResetPass()});
    this.instrumentationLevel = agentConfigs.getInstrumentationLevel();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Outcome instrument(ClassLoader loader, CtClass ctClass, String ccHash) throws Exception {
    Outcome instrument = Outcome.REJECT;

    if (this.instrumentationLevel == InstrumentationLevel.NONE) {
      // minor optimization. if no instrumentation is required, there is no need to waste cpu cycles
      // on it.
      return instrument;
    }

    if (ctClass.getClassInitializer() == null) {
      // in case there is not a static constructor, no need to perform any reset operation
      return instrument;
    }

    if (!classNoLineOfCodeMatcher.matches(ctClass)) {
      // TODO why is not Javassist able to get the class?!
      // FIXME it seems because there is no .class file for ctClass
      System.out.println("[RESETTER_INST] NO LINES OF CODE");
      return instrument;
    }

    // add an empty method called $gzoltarReseter to the {@link javassist.CtClass} object to avoid
    // *any* java.lang.NoSuchMethodError when a static field is accessed (either write or read
    // access) by another class
    ResetPass.makeEmptyResetter(ctClass);

    return super.instrument(loader, ctClass, ccHash);
  }
}
