package org.gzoltar.java11;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ModuleSystemFeaturesTest {

  private final ModuleSystemFeatures moduleSystemFeatures = new ModuleSystemFeatures();

  @Test
  public void testOwnClassBelongsToUnnamedModule() {
    assertTrue(moduleSystemFeatures.ownClassBelongsToUnnamedModule());
  }

  @Test
  public void testJdkClassBelongsToNamedModule() {
    assertTrue(moduleSystemFeatures.jdkClassBelongsToNamedModule());
  }

  @Test
  public void testJavaBaseModuleName() {
    assertEquals("java.base", moduleSystemFeatures.javaBaseModuleName());
  }

  @Test
  public void testJavaBaseExportsJavaLangPackage() {
    assertTrue(moduleSystemFeatures.javaBaseExportsJavaLangPackage());
  }

  @Test
  public void testBootLayerContainsJavaBase() {
    assertTrue(moduleSystemFeatures.bootLayerContainsJavaBase());
  }

}
