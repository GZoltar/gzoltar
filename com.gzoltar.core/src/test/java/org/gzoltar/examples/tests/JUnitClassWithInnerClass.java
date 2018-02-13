package org.gzoltar.examples.tests;

import org.junit.Assert;
import org.junit.Test;

public class JUnitClassWithInnerClass {

  public class SomeInnerClass extends JUnitClassWithInnerClass {
    // empty
  }

  public static class SomeStaticInnerClass extends JUnitClassWithInnerClass {
    @Test
    public void test_2() {
      Assert.assertNotNull(new SomeStaticInnerClass());
    }
  }

  @Test
  public void test_1() {
    Assert.assertNotNull(new SomeInnerClass());
  }
}
