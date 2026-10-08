package org.gzoltar.java25;

import java.util.Arrays;
import javax.crypto.KDF;
import javax.crypto.SecretKey;
import javax.crypto.spec.HKDFParameterSpec;

/**
 * Key Derivation Function API (JEP 510, finalized in Java 25, delivered as a
 * preview in Java 24 via JEP 478). javax.crypto.KDF provides a standard,
 * algorithm-agnostic way to derive additional cryptographic keys from an
 * initial secret, such as HKDF (RFC 5869).
 */
public class KeyDerivationFeatures {

  private static final String ALGORITHM = "HKDF-SHA256";

  public byte[] deriveKey(byte[] ikm, byte[] salt, byte[] info, int length) throws Exception {
    KDF hkdf = KDF.getInstance(ALGORITHM);
    HKDFParameterSpec spec = HKDFParameterSpec.ofExtract()
        .addIKM(ikm)
        .addSalt(salt)
        .thenExpand(info, length);
    SecretKey key = hkdf.deriveKey("Generic", spec);
    return key.getEncoded();
  }

  public boolean sameInputsProduceSameKey(byte[] ikm, byte[] salt, byte[] info, int length)
      throws Exception {
    byte[] first = deriveKey(ikm, salt, info, length);
    byte[] second = deriveKey(ikm, salt, info, length);
    return Arrays.equals(first, second);
  }

  public boolean differentSaltsProduceDifferentKeys(byte[] ikm, byte[] saltA, byte[] saltB,
      byte[] info, int length) throws Exception {
    byte[] keyA = deriveKey(ikm, saltA, info, length);
    byte[] keyB = deriveKey(ikm, saltB, info, length);
    return !Arrays.equals(keyA, keyB);
  }

  public int derivedKeyLength(byte[] ikm, byte[] salt, byte[] info, int length) throws Exception {
    return deriveKey(ikm, salt, info, length).length;
  }

}
