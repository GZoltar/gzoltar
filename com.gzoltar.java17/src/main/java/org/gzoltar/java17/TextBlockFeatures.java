package org.gzoltar.java17;

/**
 * Exercises Text Blocks (JEP 378, finalized in Java 15): a multi-line string literal delimited
 * by triple quotes, with automatic handling of indentation and embedded quotes -- no more
 * escaping every quote or concatenating "line\n" + "line\n" by hand.
 */
public class TextBlockFeatures {

  public String jsonSnippet(String name, int age) {
    return """
        {
          "name": "%s",
          "age": %d
        }""".formatted(name, age);
  }

  public String sqlSnippet(String table) {
    return """
        SELECT *
        FROM %s
        WHERE active = true""".formatted(table);
  }

}
