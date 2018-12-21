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
package com.gzoltar.core.instr.matchers;

import java.io.File;
import java.io.IOException;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;

public class SourceLocationMatcher implements IMatcher {

  private final boolean inclNoLocationClasses;

  private final List<String> buildLocations = new ArrayList<String>();

  private final ProtectionDomain protectionDomain;

  public SourceLocationMatcher(final boolean inclNoLocationClasses, final String buildLocations,
      final ProtectionDomain protectionDomain) throws IOException {
    this.inclNoLocationClasses = inclNoLocationClasses;
    for (String buildLocation : buildLocations.split(":")) {
      this.buildLocations.add(new File(buildLocation.replace(" ", "%20")).getCanonicalPath());
    }
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
    for (String buildLocation : this.buildLocations) {
      if (codeSource.getLocation().getPath().startsWith(buildLocation)) {
        return true;
      }
    }
    return false;
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
