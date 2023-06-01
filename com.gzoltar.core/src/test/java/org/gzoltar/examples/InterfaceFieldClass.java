package org.gzoltar.examples;

/**
 * No static initialization-block should be created here when instrumenting the code
 * Because an Java Interface doesn't accept any static-block in its body
 */
public interface InterfaceFieldClass {
  public final String DEFAULT_STRING = "THIS_IS_DEFAULT_STRING";
  public final String DEFAULT_STRING_WITH_NEW_OPERATOR = new String("THIS_IS_DEFAULT_STRING");
}