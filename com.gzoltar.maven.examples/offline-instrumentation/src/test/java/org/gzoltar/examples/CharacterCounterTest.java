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

