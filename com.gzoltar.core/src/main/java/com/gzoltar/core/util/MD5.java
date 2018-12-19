/**
 * Copyright (C) 2018 GZoltar contributors.
 * 
 * This file is part of GZoltar.
 * 
 * GZoltar is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * GZoltar is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with GZoltar. If
 * not, see <https://www.gnu.org/licenses/>.
 */
package com.gzoltar.core.util;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javassist.CannotCompileException;
import javassist.CtClass;

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

  public static String calculateHash(CtClass ctClass)
      throws NoSuchAlgorithmException, IOException, CannotCompileException {
    byte[] originalBytes = ctClass.toBytecode(); // toBytecode() method frozens the class
    // in order to be able to modify it, it has to be defrosted
    ctClass.defrost();
    return MD5.calculateHash(originalBytes);
  }

}
