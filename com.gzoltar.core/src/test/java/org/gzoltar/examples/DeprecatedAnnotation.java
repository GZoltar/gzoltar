package org.gzoltar.examples;

@Deprecated
public class DeprecatedAnnotation {

  @Deprecated
  public String deprecatedField = null;

  public boolean notDeprecatedMethod(int bar) {
    boolean foo = false;
    if (bar > 42) {
      foo = true;
    }
    return foo;
  }

  @Deprecated
  public boolean deprecatedMethod(int bar) {
    boolean foo = false;
    if (bar > 10) {
      foo = true;
    }
    return foo;
  }

}
