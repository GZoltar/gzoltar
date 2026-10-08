package org.gzoltar.java8;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Exercises java.util.Base64 (new in Java 8): standard encoding/decoding and the URL-safe,
 * no-padding variant. Before Java 8 this required either the javax.xml.bind DatatypeConverter
 * or a third-party library.
 */
public class Base64Features {

  public String encode(String plainText) {
    byte[] bytes = plainText.getBytes(StandardCharsets.UTF_8);
    return Base64.getEncoder().encodeToString(bytes);
  }

  public String decode(String encodedText) {
    byte[] bytes = Base64.getDecoder().decode(encodedText);
    return new String(bytes, StandardCharsets.UTF_8);
  }

  public String encodeUrlSafe(String plainText) {
    byte[] bytes = plainText.getBytes(StandardCharsets.UTF_8);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
  }

}
