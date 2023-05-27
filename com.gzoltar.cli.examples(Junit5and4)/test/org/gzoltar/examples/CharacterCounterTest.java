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
package org.gzoltar.examples;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


public class CharacterCounterTest {

  @org.junit.jupiter.api.Test
  public void test1() {
    CharacterCounter cc = new CharacterCounter();
    cc.processString("Al84");
    org.junit.jupiter.api.Assertions.assertEquals(2, cc.getNumLetters());
    org.junit.jupiter.api.Assertions.assertEquals(2, cc.getNumDigits());
    org.junit.jupiter.api.Assertions.assertEquals(0, cc.getNumOtherCharacters());
  }

  @org.junit.jupiter.api.Test
  public void test2() {
    CharacterCounter cc = new CharacterCounter();
    cc.processString("1337");
    org.junit.jupiter.api.Assertions.assertEquals(0, cc.getNumLetters());
    org.junit.jupiter.api.Assertions.assertEquals(4, cc.getNumDigits());
    org.junit.jupiter.api.Assertions.assertEquals(0, cc.getNumOtherCharacters());
  }

  @org.junit.jupiter.api.Test
  public void test3() {
    CharacterCounter cc = new CharacterCounter();
    cc.processString("T1");
    org.junit.jupiter.api.Assertions.assertEquals(1, cc.getNumLetters());
    org.junit.jupiter.api.Assertions.assertEquals(1, cc.getNumDigits());
    org.junit.jupiter.api.Assertions.assertEquals(0, cc.getNumOtherCharacters());
  }

  @org.junit.jupiter.api.Test
  public void test4() {
    CharacterCounter cc = new CharacterCounter();
    cc.processString("UB40");
    org.junit.jupiter.api.Assertions.assertEquals(2, cc.getNumLetters());
    org.junit.jupiter.api.Assertions.assertEquals(2, cc.getNumDigits());
    org.junit.jupiter.api.Assertions.assertEquals(0, cc.getNumOtherCharacters());
  }

  @org.junit.Test
  public void test5() {
    CharacterCounter cc = new CharacterCounter();
    cc.processString("#lolcats");
    org.junit.Assert.assertEquals(7, cc.getNumLetters());
    org.junit.Assert.assertEquals(0, cc.getNumDigits());
    org.junit.Assert.assertEquals(1, cc.getNumOtherCharacters());
  }

  /*@Test
  public void test6() {
    CharacterCounter cc = new CharacterCounter();
    cc.processString("\0x10\0x05\0x00");
    assertEquals(0, cc.getNumLetters());
    assertEquals(0, cc.getNumDigits());
    assertEquals(3, cc.getNumOtherCharacters());
  }*/

  @org.junit.Test
  public void test7() {
    CharacterCounter cc = new CharacterCounter();
    cc.processString("GZoltar");
    org.junit.Assert.assertEquals(7, cc.getNumLetters());
    org.junit.Assert.assertEquals(0, cc.getNumDigits());
    org.junit.Assert.assertEquals(0, cc.getNumOtherCharacters());
  }

  @org.junit.Test
  public void test8() {
    CharacterCounter cc = new CharacterCounter();
    cc.processString("");
    org.junit.Assert.assertEquals(0, cc.getNumLetters());
    org.junit.Assert.assertEquals(0, cc.getNumDigits());
    org.junit.Assert.assertEquals(0, cc.getNumOtherCharacters());
  }

}