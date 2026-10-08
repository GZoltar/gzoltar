package org.gzoltar.java11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Exercises the Files.readString/writeString convenience methods added in Java 11 -- before
 * this, reading a whole file into a String required manual byte[]/Charset handling.
 */
public class FileIOFeatures {

  public void writeText(Path file, String content) throws IOException {
    Files.writeString(file, content);
  }

  public String readText(Path file) throws IOException {
    return Files.readString(file);
  }

  public int countWords(Path file) throws IOException {
    String content = Files.readString(file);
    if (content.isBlank()) {
      return 0;
    }
    return content.trim().split("\\s+").length;
  }

}
