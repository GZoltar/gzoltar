package org.gzoltar.examples;

/**
 * Example adapted from the research paper 'Adaptive Random Test Case Prioritization'.
 */
public class CharacterCounter {

  private int numLetters;

  private int numDigits;

  private int other;

  public CharacterCounter() {
    this.numLetters = 0;
    this.numDigits = 0;
    this.other = 0;
  }

  public void processString(String str) {
    for (char c : str.toCharArray()) {
      if ('A' <= c && 'Z' >= c) {
        this.numLetters += 2; /* FAULT */
      } else if ('a' <= c && 'z' >= c) {
        this.numLetters += 1;
      } else if ('0' <= c && '9' >= c) {
        this.numDigits += 1;
      } else {
        this.other += 1;
      }
    }
  }

  public int getNumLetters() {
    return this.numLetters;
  }

  public int getNumDigits() {
    return this.numDigits;
  }

  public int getNumOtherCharacters() {
    return this.other;
  }

}

