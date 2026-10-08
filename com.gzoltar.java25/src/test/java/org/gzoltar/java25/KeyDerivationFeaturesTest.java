package org.gzoltar.java25;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.charset.StandardCharsets;
import org.junit.Test;

public class KeyDerivationFeaturesTest {

  private final KeyDerivationFeatures keyDerivationFeatures = new KeyDerivationFeatures();

  private static final byte[] IKM = "input-keying-material".getBytes(StandardCharsets.UTF_8);
  private static final byte[] SALT_A = "salt-a".getBytes(StandardCharsets.UTF_8);
  private static final byte[] SALT_B = "salt-b".getBytes(StandardCharsets.UTF_8);
  private static final byte[] INFO = "context-info".getBytes(StandardCharsets.UTF_8);

  @Test
  public void testSameInputsProduceSameKey() throws Exception {
    assertTrue(keyDerivationFeatures.sameInputsProduceSameKey(IKM, SALT_A, INFO, 32));
  }

  @Test
  public void testDifferentSaltsProduceDifferentKeys() throws Exception {
    assertTrue(keyDerivationFeatures.differentSaltsProduceDifferentKeys(IKM, SALT_A, SALT_B, INFO, 32));
  }

  @Test
  public void testDerivedKeyLength() throws Exception {
    assertEquals(32, keyDerivationFeatures.derivedKeyLength(IKM, SALT_A, INFO, 32));
  }

  @Test
  public void testDerivedKeyLength_DifferentSize() throws Exception {
    assertEquals(16, keyDerivationFeatures.derivedKeyLength(IKM, SALT_A, INFO, 16));
  }

}
