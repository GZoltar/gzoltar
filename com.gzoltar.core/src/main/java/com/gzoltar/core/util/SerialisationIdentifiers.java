package com.gzoltar.core.util;

public final class SerialisationIdentifiers {

  /** File format version, will be incremented for each incompatible change. */
  public static final char FORMAT_VERSION;

  static {
    // Runtime initialise to ensure the compiler does not inline the value.
    FORMAT_VERSION = 0x0001;
  }

  /** Magic number in header for file format identification. */
  public static final char MAGIC_NUMBER = 0xC0C0;

  /** Block identifier for file headers. */
  public static final byte BLOCK_HEADER = 0x01;

  /** Block identifier for nodes information. */
  public static final byte BLOCK_NODE = 0x10;

  /** Block identifier for transaction information. */
  public static final byte BLOCK_TRANSACTION = 0x11;

}
