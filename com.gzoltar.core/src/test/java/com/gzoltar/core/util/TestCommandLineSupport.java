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
package com.gzoltar.core.util;

import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import org.junit.Test;

public class TestCommandLineSupport {

  @Test
  public void testQuote1() {
    assertEquals("aBc", CommandLineSupport.quote("aBc"));
  }

  @Test
  public void testQuote2() {
    assertEquals("\"a c\"", CommandLineSupport.quote("a c"));
  }

  @Test
  public void testQuote3() {
    assertEquals("\"a\\\"c\"", CommandLineSupport.quote("a\"c"));
  }

  @Test
  public void testQuote4() {
    assertEquals("\" xy \"", CommandLineSupport.quote(" xy "));
  }

  @Test
  public void testQuote5() {
    assertEquals("a\\\\b", CommandLineSupport.quote("a\\b"));
  }

  @Test
  public void testQuoteList1() {
    assertEquals("", CommandLineSupport.quote(Arrays.<String>asList()));
  }

  @Test
  public void testQuoteList2() {
    assertEquals("a", CommandLineSupport.quote(Arrays.asList("a")));
  }

  @Test
  public void testQuoteList3() {
    assertEquals("a b c", CommandLineSupport.quote(Arrays.asList("a", "b", "c")));
  }

  @Test
  public void testQuoteList4() {
    assertEquals("a \"b b\" c", CommandLineSupport.quote(Arrays.asList("a", "b b", "c")));
  }

  @Test
  public void testSplit1() {
    assertEquals(Arrays.asList(), CommandLineSupport.split(null));
  }

  @Test
  public void testSplit2() {
    assertEquals(Arrays.asList(), CommandLineSupport.split(""));
  }

  @Test
  public void testSplit3() {
    assertEquals(Arrays.asList("abc"), CommandLineSupport.split("abc"));
  }

  @Test
  public void testSplit4() {
    assertEquals(Arrays.asList("aa", "bbbb", "cccccc"),
        CommandLineSupport.split("  aa  bbbb  cccccc   "));
  }

  @Test
  public void testSplit5() {
    assertEquals(Arrays.asList("a a", "b b "), CommandLineSupport.split("\"a a\" \"b b \" "));
  }

  @Test
  public void testSplit6() {
    assertEquals(Arrays.asList("a\"c"), CommandLineSupport.split("a\\\"c"));
  }

  @Test
  public void testSplit7() {
    assertEquals(Arrays.asList("a\\c"), CommandLineSupport.split("a\\c"));
  }

  @Test
  public void testSplit8() {
    assertEquals(Arrays.asList("a\\"), CommandLineSupport.split("a\\"));
  }

  @Test
  public void testSplit9() {
    assertEquals(Arrays.asList("a\\", "b"), CommandLineSupport.split("a\\ b"));
  }

  @Test
  public void testSplit10() {
    assertEquals(Arrays.asList("a\\b"), CommandLineSupport.split("a\\\\b"));
  }

}
