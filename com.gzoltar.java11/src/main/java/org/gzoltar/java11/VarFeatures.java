package org.gzoltar.java11;

import java.util.function.BiFunction;

/**
 * Exercises `var` for lambda parameters (JEP 323, Java 11) -- lets you attach modifiers to a
 * lambda's parameters the same way you would for a regular method parameter. (Plain `var` for
 * local variables is a Java 10 feature and is intentionally left out of this Java 11-only
 * project.)
 */
public class VarFeatures {

  public int combineWithVarLambda(int a, int b) {
    BiFunction<Integer, Integer, Integer> add = (var x, var y) -> x + y;
    return add.apply(a, b);
  }

}
