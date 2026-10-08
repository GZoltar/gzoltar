package org.gzoltar.java8;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class Base64FeaturesTest {

  private final Base64Features base64Features = new Base64Features();

  @Test
  public void testEncode_KnownValue() {
    assertEquals("Z3pvbHRhcg==", base64Features.encode("gzoltar"));
  }

  @Test
  public void testDecode_KnownValue() {
    assertEquals("gzoltar", base64Features.decode("Z3pvbHRhcg=="));
  }

  @Test
  public void testEncodeThenDecode_RoundTrip() {
    String original = "GZoltar + Java 8 = coverage!";
    String encoded = base64Features.encode(original);
    assertEquals(original, base64Features.decode(encoded));
  }

  @Test
  public void testEncodeUrlSafe_NoPaddingOrUnsafeChars() {
    String encoded = base64Features.encodeUrlSafe("gzoltar>>??");
    assertFalse(encoded.contains("="));
    assertFalse(encoded.contains("+"));
    assertFalse(encoded.contains("/"));
  }

}
