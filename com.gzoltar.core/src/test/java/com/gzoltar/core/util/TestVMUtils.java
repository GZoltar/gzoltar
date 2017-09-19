package com.gzoltar.core.util;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TestVMUtils {

  @Test
  public void testEmptyClassName() {
    assertEquals("", VMUtils.toVMName(""));
  }

  @Test
  public void testValidClassName() {
    assertEquals("foo/bar/MyClass", VMUtils.toVMName("foo.bar.MyClass"));
  }

}
