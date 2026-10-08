package org.gzoltar.java21;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterEmojiFeaturesTest {

  private final CharacterEmojiFeatures characterEmojiFeatures = new CharacterEmojiFeatures();

  private static final int GRINNING_FACE = 0x1F600; // 😀
  private static final int PARTY_POPPER = 0x1F389; // 🎉
  private static final int SKIN_TONE_MODIFIER = 0x1F3FB; // Fitzpatrick type-1-2

  @Test
  public void testIsEmoji_True() {
    assertTrue(characterEmojiFeatures.isEmoji(GRINNING_FACE));
  }

  @Test
  public void testIsEmoji_False_RegularLetter() {
    assertFalse(characterEmojiFeatures.isEmoji('A'));
  }

  @Test
  public void testIsEmojiPresentation_True() {
    assertTrue(characterEmojiFeatures.isEmojiPresentation(GRINNING_FACE));
  }

  @Test
  public void testIsEmojiModifier_True() {
    assertTrue(characterEmojiFeatures.isEmojiModifier(SKIN_TONE_MODIFIER));
  }

  @Test
  public void testIsEmojiModifier_False_RegularLetter() {
    assertFalse(characterEmojiFeatures.isEmojiModifier('B'));
  }

  @Test
  public void testCountEmojis() {
    String text = "Hello " + new String(Character.toChars(GRINNING_FACE))
        + " World " + new String(Character.toChars(PARTY_POPPER));
    assertEquals(2, characterEmojiFeatures.countEmojis(text));
  }

  @Test
  public void testCountEmojis_NoEmojis() {
    assertEquals(0, characterEmojiFeatures.countEmojis("plain text, no emojis here"));
  }
}
