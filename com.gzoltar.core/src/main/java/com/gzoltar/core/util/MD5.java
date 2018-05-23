package com.gzoltar.core.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class MD5 {

  /**
   * Computes the md5 hash of a byte array.
   * 
   * @param bytes array of bytes
   * @return the md5 hash of a byte array
   * @throws NoSuchAlgorithmException if MD5 algorithm is not available.
   */
  public static String calculateHash(byte[] bytes) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(bytes);

    // get the hash's bytes
    byte[] hashBytes = md.digest();
    // convert bytes to hexadecimal format
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < hashBytes.length; i++) {
      sb.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1));
    }

    return sb.toString();
  }

}
