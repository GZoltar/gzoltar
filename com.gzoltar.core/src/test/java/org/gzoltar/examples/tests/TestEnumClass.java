package org.gzoltar.examples.tests;

import static org.junit.Assert.assertNotNull;
import org.gzoltar.examples.EnumClass;
import org.junit.Test;

public class TestEnumClass {

  @Test
  public void test() {
    assertNotNull(EnumClass.FIRST);
  }

}
