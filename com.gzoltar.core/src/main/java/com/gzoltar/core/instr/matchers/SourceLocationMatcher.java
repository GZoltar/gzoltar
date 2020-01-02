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

import java.security.CodeSource;
import java.security.ProtectionDomain;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;

public class SourceLocationMatcher implements IMatcher {

  private final boolean inclNoLocationClasses;

  private final String buildLocation;

  private final ProtectionDomain protectionDomain;

  public SourceLocationMatcher(final boolean inclNoLocationClasses, final String buildLocation,
      final ProtectionDomain protectionDomain) {
    this.inclNoLocationClasses = inclNoLocationClasses;
    this.buildLocation = buildLocation.replace(" ", "%20");
    this.protectionDomain = protectionDomain;
  }

  @Override
  public boolean matches(final CtClass ctClass) {
    if (!this.inclNoLocationClasses && !this.hasSourceLocation(this.protectionDomain)) {
      return false;
    }
    return true;
  }

  /**
   * Checks whether this protection domain is associated with a source location.
   * 
   * @param protectionDomain protection domain to check (or <code>null</code>)
   * @return <code>true</code> if a source location is defined
   */
  private boolean hasSourceLocation(final ProtectionDomain protectionDomain) {
    if (protectionDomain == null) {
      return false;
    }
    final CodeSource codeSource = protectionDomain.getCodeSource();
    if (codeSource == null) {
      return false;
    }
    if (codeSource.getLocation() == null) {
      return false;
    }
    return codeSource.getLocation().getPath().startsWith(this.buildLocation);
  }

  @Override
  public boolean matches(final CtBehavior ctBehavior) {
    return this.matches(ctBehavior.getDeclaringClass());
  }

  @Override
  public boolean matches(final CtField ctField) {
    return this.matches(ctField.getDeclaringClass());
  }

}
