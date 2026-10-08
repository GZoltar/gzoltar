package org.gzoltar.java11;

/**
 * Exercises the reflection side of the Java Platform Module System (JPMS / "Project Jigsaw",
 * JEP 261, Java 9): java.lang.Module lets code inspect module boundaries at runtime -- whether a
 * class belongs to a named or unnamed module, what a module is called, and which packages it
 * exports.
 *
 * Note: this project itself is intentionally NOT turned into a JPMS module (no module-info.java
 * was added). Doing so would risk breaking GZoltar's own agent-based instrumentation and JUnit's
 * reflection-based test runner, both of which rely on unrestricted reflective access across the
 * classpath -- a real, known source of friction between JPMS and bytecode/reflection tooling.
 * Instead, this class exercises the actual Module API surface introduced in Java 9 by inspecting
 * both our own (unnamed) module and the JDK's own (named) java.base module.
 */
public class ModuleSystemFeatures {

  public boolean ownClassBelongsToUnnamedModule() {
    return !ModuleSystemFeatures.class.getModule().isNamed();
  }

  public boolean jdkClassBelongsToNamedModule() {
    return String.class.getModule().isNamed();
  }

  public String javaBaseModuleName() {
    return String.class.getModule().getName();
  }

  public boolean javaBaseExportsJavaLangPackage() {
    return String.class.getModule().isExported("java.lang");
  }

  public boolean bootLayerContainsJavaBase() {
    return ModuleLayer.boot().findModule("java.base").isPresent();
  }

}
