/**
 * Copyright (C) 2020 GZoltar contributors.
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
package com.gzoltar.agent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.junit.Test;

/**
 * Unit tests for {@link com.gzoltar.agent.AgentJar}.
 */
public class TestAgentJar {

  @Test
  public void testResourceAsStream() throws IOException {
    final InputStream in = AgentJar.getResourceAsStream();
    assertAgentContents(in);
  }

  @Test
  public void extractToTmpDir() throws IOException {
    final File file = AgentJar.extractToTempLocation();
    assertAgentContents(new FileInputStream(file));
    file.delete();
  }

  @Test
  public void testExtractTo() throws IOException {
    final File file = File.createTempFile("agent", ".jar");
    AgentJar.extractTo(file);
    assertAgentContents(new FileInputStream(file));
    file.delete();
  }

  private void assertAgentContents(final InputStream in) throws IOException {
    final ZipInputStream zip = new ZipInputStream(in);
    while (zip.available() == 1) {
      final ZipEntry entry = zip.getNextEntry();
      assertNotNull("Manifest not found.", entry);
      if ("META-INF/MANIFEST.MF".equals(entry.getName())) {
        final Manifest manifest = new Manifest(zip);
        assertEquals("GZoltar Agent RT",
            manifest.getMainAttributes().getValue("Implementation-Title"));
        in.close();
        return;
      }
    }
  }
}
