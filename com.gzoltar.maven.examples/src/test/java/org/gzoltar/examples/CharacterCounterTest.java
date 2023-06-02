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

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CharacterCounterTest {

  @Test
  public void test1() {
    CharacterCounter cc = new CharacterCounter();
    cc.processString("Al84");
    assertEquals(2, cc.getNumLetters());
    assertEquals(2, cc.getNumDigits());
    assertEquals(0, cc.getNumOtherCharacters());
  }

  @Test
  public void test2() {
    CharacterCounter cc = new CharacterCounter();
    cc.processString("1337");
    assertEquals(0, cc.getNumLetters());
    assertEquals(4, cc.getNumDigits());
    assertEquals(0, cc.getNumOtherCharacters());
  }

  @Test
  public void test3() {
    CharacterCounter cc = new CharacterCounter();
    cc.processString("T1");
    assertEquals(1, cc.getNumLetters());
    assertEquals(1, cc.getNumDigits());
    assertEquals(0, cc.getNumOtherCharacters());
  }

  @Test
  public void test4() {
    CharacterCounter cc = new CharacterCounter();
    cc.processString("UB40");
    assertEquals(2, cc.getNumLetters());
    assertEquals(2, cc.getNumDigits());
    assertEquals(0, cc.getNumOtherCharacters());
  }

  @Test
  public void test5() {
    CharacterCounter cc = new CharacterCounter();
    cc.processString("#lolcats");
    assertEquals(7, cc.getNumLetters());
    assertEquals(0, cc.getNumDigits());
    assertEquals(1, cc.getNumOtherCharacters());
  }

  /*@Test
  public void test6() {
    CharacterCounter cc = new CharacterCounter();
    cc.processString("\0x10\0x05\0x00");
    assertEquals(0, cc.getNumLetters());
    assertEquals(0, cc.getNumDigits());
    assertEquals(3, cc.getNumOtherCharacters());
  }*/

  @Test
  public void test7() {
    CharacterCounter cc = new CharacterCounter();
    cc.processString("GZoltar");
    assertEquals(7, cc.getNumLetters());
    assertEquals(0, cc.getNumDigits());
    assertEquals(0, cc.getNumOtherCharacters());
  }

  @Test
  public void test8() {
    CharacterCounter cc = new CharacterCounter();
    cc.processString("");
    assertEquals(0, cc.getNumLetters());
    assertEquals(0, cc.getNumDigits());
    assertEquals(0, cc.getNumOtherCharacters());
  }

}

