package org.gzoltar.java11;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Path;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class FileIOFeaturesTest {

  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();

  private final FileIOFeatures fileIOFeatures = new FileIOFeatures();

  @Test
  public void testWriteThenRead_RoundTrip() throws IOException {
    Path file = temporaryFolder.newFile("gzoltar.txt").toPath();
    fileIOFeatures.writeText(file, "hello gzoltar");
    assertEquals("hello gzoltar", fileIOFeatures.readText(file));
  }

  @Test
  public void testCountWords() throws IOException {
    Path file = temporaryFolder.newFile("words.txt").toPath();
    fileIOFeatures.writeText(file, "GZoltar supports Java 11 features");
    assertEquals(5, fileIOFeatures.countWords(file));
  }

  @Test
  public void testCountWords_EmptyFile() throws IOException {
    Path file = temporaryFolder.newFile("empty.txt").toPath();
    fileIOFeatures.writeText(file, "");
    assertEquals(0, fileIOFeatures.countWords(file));
  }

}
