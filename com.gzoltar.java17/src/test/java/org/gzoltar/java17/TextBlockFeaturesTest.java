package org.gzoltar.java17;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TextBlockFeaturesTest {

  private final TextBlockFeatures textBlockFeatures = new TextBlockFeatures();

  @Test
  public void testJsonSnippet_ContainsFormattedValues() {
    String json = textBlockFeatures.jsonSnippet("gzoltar", 7);
    assertTrue(json.contains("\"name\": \"gzoltar\""));
    assertTrue(json.contains("\"age\": 7"));
  }

  @Test
  public void testSqlSnippet_ContainsTableName() {
    String sql = textBlockFeatures.sqlSnippet("products");
    assertTrue(sql.contains("FROM products"));
    assertTrue(sql.contains("WHERE active = true"));
  }

}
