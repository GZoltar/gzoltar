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
package com.gzoltar.core.instr.filter;

import com.gzoltar.core.instr.Outcome;
import com.gzoltar.core.util.ClassUtils;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * Filters the implicitly declared accessor, equals, hashCode and toString methods of a record
 * (Java 16+).
 *
 * When a record does not provide its own implementation of these methods, the compiler generates
 * them automatically. Unlike most other compiler-generated methods (e.g., a lambda's synthetic
 * method), these are *not* marked ACC_SYNTHETIC -- this was an explicit design decision so that
 * tools such as debuggers and reflection APIs treat them as ordinary members of the record. One
 * consequence, however, is that none of them has its own line in the source code: every one of
 * them is attributed to the exact same single line, the record's own declaration. Without this
 * filter, GZoltar would report one "component" per generated method (accessor(s), equals,
 * hashCode, toString) all pointing at that one line, which is redundant and can be misleading in
 * a fault-localization report: several unrelated-looking entries that are, in fact, the very same
 * line of code counted multiple times.
 *
 * Note: if a record's author explicitly overrides one of these methods with their own
 * implementation, this filter has no way to tell that apart from the implicit case using only the
 * compiled bytecode, so it will also (harmlessly) exclude that explicit override. This mirrors the
 * same trade-off {@link EnumFilter} already makes for an enum's 'values'/'valueOf' methods.
 */
public final class RecordFilter extends Filter {

  @Override
  public Outcome filter(final CtClass ctClass) {
    return Outcome.ACCEPT;
  }

  @Override
  public Outcome filter(final CtBehavior ctBehavior) {
    CtClass declaringClass = ctBehavior.getDeclaringClass();
    if (!(ctBehavior instanceof CtMethod) || !ClassUtils.isRecord(declaringClass)) {
      return Outcome.ACCEPT;
    }

    CtMethod ctMethod = (CtMethod) ctBehavior;
    String name = ctMethod.getName();

    try {
      CtClass[] parameterTypes = ctMethod.getParameterTypes();

      if (parameterTypes.length == 0) {
        if ("hashCode".equals(name) || "toString".equals(name)) {
          return Outcome.REJECT;
        }
        // an implicitly declared accessor method shares its name with one of the record's own
        // components, i.e., with one of the class' own declared fields
        for (CtField field : declaringClass.getDeclaredFields()) {
          if (field.getName().equals(name)) {
            return Outcome.REJECT;
          }
        }
      } else if (parameterTypes.length == 1 && "equals".equals(name)
          && "java.lang.Object".equals(parameterTypes[0].getName())) {
        return Outcome.REJECT;
      }
    } catch (NotFoundException e) {
      // if the parameter types cannot be resolved, err on the safe side and instrument the method
      return Outcome.ACCEPT;
    }

    return Outcome.ACCEPT;
  }

}
