package com.gzoltar.core.instr.matchers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TestWildcardMatcher {

  @Test
  public void testEmpty() {
    assertTrue(new ClassNameMatcher("").matches(""));
    assertFalse(new ClassNameMatcher("").matches("abc"));
  }

  @Test
  public void testExact() {
    assertTrue(new ClassNameMatcher("abc/def.txt").matches("abc/def.txt"));
  }

  @Test
  public void testCaseSensitive() {
    assertFalse(new ClassNameMatcher("abcdef").matches("abcDef"));
    assertFalse(new ClassNameMatcher("ABCDEF").matches("AbCDEF"));
  }

  @Test
  public void testQuote() {
    assertFalse(new ClassNameMatcher("rst.xyz").matches("rstAxyz"));
    assertTrue(new ClassNameMatcher("(x)+").matches("(x)+"));
  }

  @Test
  public void testWildcards() {
    assertTrue(new ClassNameMatcher("*").matches(""));
    assertTrue(new ClassNameMatcher("*").matches("java/lang/Object"));
    assertTrue(new ClassNameMatcher("*Test*").matches("gzoltar/TestWildcardMatcher"));
    assertTrue(new ClassNameMatcher("Matcher*").matches("Matcher"));
    assertTrue(new ClassNameMatcher("Matcher*").matches("MatcherTest"));
    assertTrue(new ClassNameMatcher("a*b*a").matches("a-b-b-a"));
    assertFalse(new ClassNameMatcher("a*b*a").matches("alaska"));
    assertTrue(new ClassNameMatcher("Hello?orld").matches("HelloWorld"));
    assertFalse(new ClassNameMatcher("Hello?orld").matches("HelloWWWorld"));
    assertTrue(new ClassNameMatcher("?zolt*").matches("gzoltar"));
  }

  @Test
  public void testMultiExpression() {
    assertTrue(new ClassNameMatcher("Hello:World").matches("World"));
    assertTrue(new ClassNameMatcher("Hello:World").matches("World"));
    assertTrue(new ClassNameMatcher("*Test:*Foo").matches("UnitTest"));
  }

  @Test
  public void testDollar() {
    assertTrue(new ClassNameMatcher("*$*").matches("java/util/Map$Entry"));
    assertTrue(new ClassNameMatcher("*$$$*").matches("org/example/Enity$$$generated123"));
  }

  @Test
  public void testInner() {
    assertFalse(new ClassNameMatcher("com.gzoltar.core.instr.matchers.AbstractWildcardMatcher")
        .matches("com.gzoltar.OtherClass"));
    assertTrue(new ClassNameMatcher("com.gzoltar.core.instr.matchers.AbstractWildcardMatcher")
        .matches("com.gzoltar.core.instr.matchers.AbstractWildcardMatcher"));
    assertTrue(new ClassNameMatcher("com.gzoltar.core.instr.matchers.AbstractWildcardMatcher$*")
        .matches("com.gzoltar.core.instr.matchers.AbstractWildcardMatcher$innerClass"));
  }

}
