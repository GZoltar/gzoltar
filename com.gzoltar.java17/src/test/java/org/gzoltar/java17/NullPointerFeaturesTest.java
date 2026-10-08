package org.gzoltar.java17;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NullPointerFeaturesTest {

  private final NullPointerFeatures nullPointerFeatures = new NullPointerFeatures();

  @Test
  public void testHelpfulMessage_MentionsGetInner() {
    NullPointerFeatures.Box box = new NullPointerFeatures.Box();
    String message = nullPointerFeatures.helpfulMessageFor(box);
    assertNotNull(message);
    assertTrue(message.contains("getInner()"));
  }

}
