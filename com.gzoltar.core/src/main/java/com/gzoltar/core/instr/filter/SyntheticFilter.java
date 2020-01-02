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
import com.gzoltar.core.instr.actions.BlackList;
import com.gzoltar.core.instr.matchers.AndMatcher;
import com.gzoltar.core.instr.matchers.ClassAttributeMatcher;
import com.gzoltar.core.instr.matchers.ClassModifierMatcher;
import com.gzoltar.core.instr.matchers.MethodAttributeMatcher;
import com.gzoltar.core.instr.matchers.MethodModifierMatcher;
import com.gzoltar.core.instr.matchers.NotMatcher;
import com.gzoltar.core.instr.matchers.OrMatcher;
import com.gzoltar.core.instr.matchers.PrefixMatcher;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.bytecode.AccessFlag;
import javassist.bytecode.SyntheticAttribute;

/**
 * Filters synthetic classes or synthetic methods unless they represent bodies of lambda expressions.
 */
public final class SyntheticFilter extends Filter {

  private static final BlackList SYNTHETIC_CLASS =
      new BlackList(new OrMatcher(new ClassModifierMatcher(AccessFlag.BRIDGE),
          new ClassModifierMatcher(AccessFlag.SYNTHETIC),
          new ClassAttributeMatcher(SyntheticAttribute.tag)));

  private static final BlackList SYNTHETIC_METHOD = new BlackList(new AndMatcher(
      new OrMatcher(new MethodModifierMatcher(AccessFlag.BRIDGE),
          new MethodModifierMatcher(AccessFlag.SYNTHETIC),
          new MethodAttributeMatcher(SyntheticAttribute.tag)),
      new NotMatcher(new PrefixMatcher("lambda$"))));

  /**
   * {@inheritDoc}
   */
  @Override
  public Outcome filter(final CtClass ctClass) {
    return super.filter(ctClass, SYNTHETIC_CLASS);
  }

  /**
   * Suppose we compile the following snippet of code with a JDK version < 5.
   * 
   * public class Main {
   *   public static void main(String[] args) {
   *     // print Class object of Main class
   *     System.out.println(Main.class);
   *   }
   * }
   * 
   * As javac of, for example, JDK-4 does not support 'class literals', it creates a new static
   * method called 'class$' which returns a java.lang.Class<?>. that method will be called when
   * 'Main.class' code is executed. However, that method does not exist in the source-code, it
   * only exists in bytecode. As GZoltar relies on bytecode it assumes that method 'class$' is to
   * instrument as any other method. With Java-5 a new variant on ldc_w has been defined to
   * implement class literals, and therefore this is not a problem anymore.
   * 
   * Check out https://blogs.oracle.com/sundararajan/entry/class_literals_in_jdk_1 for more
   * information.
   * 
   * 
   * Another example of synthetic methods are the methods called "access$[0-9]*" which allow class
   * access to private fields from anonymous inner classes. Example:
   * 
   * class Access {
   *   private String y = "y";
   *   public static void x() {
   *      final access a = new Access();
   *      Object o = new Object() {
   *        public String toString() {
   *            return a.y;
   *        }
   *      };
   *   }
   * }
   * 
   * The anonymous Object subclass only has private access in code here. The compiler generates a
   * static method in Access which simply returns the field from the passed in object. The
   * compiler also replaces the call to "a.y" with "access$000(a)".
   */
  @Override
  public Outcome filter(final CtBehavior ctBehavior) {
    return super.filter(ctBehavior, SYNTHETIC_METHOD);
  }

}
