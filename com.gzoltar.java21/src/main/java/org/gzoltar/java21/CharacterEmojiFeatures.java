package org.gzoltar.java21;

/**
 * Improved Emoji support in java.lang.Character (JDK-8303018, added in Java 21,
 * part of Unicode 15.0 support). Adds isEmoji(int), isEmojiPresentation(int),
 * isEmojiModifier(int), isEmojiModifierBase(int), isEmojiComponent(int) and
 * isExtendedPictographic(int).
 */
public class CharacterEmojiFeatures {

  public boolean isEmoji(int codePoint) {
    return Character.isEmoji(codePoint);
  }

  public boolean isEmojiPresentation(int codePoint) {
    return Character.isEmojiPresentation(codePoint);
  }

  public boolean isEmojiModifier(int codePoint) {
    return Character.isEmojiModifier(codePoint);
  }

  /** Counts how many emoji code points appear in the given text. */
  public long countEmojis(String text) {
    return text.codePoints().filter(Character::isEmoji).count();
  }
}
