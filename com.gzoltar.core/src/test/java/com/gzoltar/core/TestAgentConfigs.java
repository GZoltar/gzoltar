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
package com.gzoltar.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.io.File;
import org.junit.Test;
import com.gzoltar.core.instr.granularity.GranularityLevel;

public class TestAgentConfigs {

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidSyntaxConfiguration() {
    new AgentConfigs("foo~bar");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUnknownConfiguration() {
    new AgentConfigs("foo=bar");
  }

  private void defaultValues(final String configs) {
    AgentConfigs agentConfigs = new AgentConfigs(configs);
    assertEquals(AgentConfigs.DEFAULT_BUILDLOCATION, agentConfigs.getBuildLocation());
    assertEquals(AgentConfigs.DEFAULT_DESTFILE, agentConfigs.getDestfile());
    assertEquals(AgentConfigs.DEFAULT_INCLUDES, agentConfigs.getIncludes());
    assertEquals(AgentConfigs.DEFAULT_EXCLUDES, agentConfigs.getExcludes());
    assertEquals(AgentConfigs.DEFAULT_EXCLCLASSLOADER, agentConfigs.getExclClassloader());
    assertEquals(AgentConfigs.DEFAULT_INCLNOLOCATIONCLASSES,
        agentConfigs.getInclNoLocationClasses());
    assertEquals(AgentConfigs.DEFAULT_OUTPUT, agentConfigs.getOutput());
    assertEquals(AgentConfigs.DEFAULT_GRANULARITY, agentConfigs.getGranularity());
    assertEquals(AgentConfigs.DEFAULT_INCLPUBLICMETHODS, agentConfigs.getInclPublicMethods());
  }

  @Test
  public void testNullConfigs() {
    this.defaultValues(null);
  }

  @Test
  public void testEmptyStringConfigs() {
    this.defaultValues("");
  }

  @Test
  public void testBuildLocation() {
    String key = AgentConfigs.BUILDLOCATION_KEY;
    String value = "foo";
    AgentConfigs agentConfigs = new AgentConfigs(key + "=" + value);
    assertEquals(value, agentConfigs.getBuildLocation());
  }

  @Test
  public void testSetBuildLocation() {
    String value = "foo";
    AgentConfigs agentConfigs = new AgentConfigs();
    agentConfigs.setBuildLocation(value);
    assertEquals(value, agentConfigs.getBuildLocation());
  }

  @Test
  public void testDestFile() {
    String key = AgentConfigs.DESTFILE_KEY;
    String value = "foo";
    AgentConfigs agentConfigs = new AgentConfigs(key + "=" + value);
    assertEquals(value, agentConfigs.getDestfile());

    agentConfigs = new AgentConfigs();
    agentConfigs.setDestfile(value);
    assertEquals(value, agentConfigs.getDestfile());
  }

  @Test
  public void testIncludes() {
    String key = AgentConfigs.INCLUDES_KEY;
    String value = "foo";
    AgentConfigs agentConfigs = new AgentConfigs(key + "=" + value);
    assertEquals(value, agentConfigs.getIncludes());

    agentConfigs = new AgentConfigs();
    agentConfigs.setIncludes(value);
    assertEquals(value, agentConfigs.getIncludes());
  }

  @Test
  public void testExcludes() {
    String key = AgentConfigs.EXCLUDES_KEY;
    String value = "foo";
    AgentConfigs agentConfigs = new AgentConfigs(key + "=" + value);
    assertEquals(value, agentConfigs.getExcludes());

    agentConfigs = new AgentConfigs();
    agentConfigs.setExcludes(value);
    assertEquals(value, agentConfigs.getExcludes());
  }

  @Test
  public void testExclClassloader() {
    String key = AgentConfigs.EXCLCLASSLOADER_KEY;
    String value = "foo";
    AgentConfigs agentConfigs = new AgentConfigs(key + "=" + value);
    assertEquals(value, agentConfigs.getExclClassloader());

    agentConfigs = new AgentConfigs();
    agentConfigs.setExclClassloader(value);
    assertEquals(value, agentConfigs.getExclClassloader());
  }

  @Test
  public void testInclNoLocationClasses() {
    String key = AgentConfigs.INCLNOLOCATIONCLASSES_KEY;
    Boolean value = true;
    AgentConfigs agentConfigs = new AgentConfigs(key + "=" + value);
    assertEquals(value, agentConfigs.getInclNoLocationClasses());

    agentConfigs = new AgentConfigs();
    agentConfigs.setInclNoLocationClasses(value);
    assertEquals(value, agentConfigs.getInclNoLocationClasses());
  }

  @Test
  public void testOutput() {
    String key = AgentConfigs.OUTPUT_KEY;
    AgentOutput value = AgentOutput.FILE;
    AgentConfigs agentConfigs = new AgentConfigs(key + "=" + value);
    assertEquals(value, agentConfigs.getOutput());

    agentConfigs = new AgentConfigs();
    agentConfigs.setOutput(value);
    assertEquals(value, agentConfigs.getOutput());

    agentConfigs = new AgentConfigs();
    agentConfigs.setOutput(value.toString());
    assertEquals(value, agentConfigs.getOutput());
  }

  @Test
  public void testGranularity() {
    String key = AgentConfigs.GRANULARITY_KEY;
    GranularityLevel value = GranularityLevel.BASICBLOCK;
    AgentConfigs agentConfigs = new AgentConfigs(key + "=" + value);
    assertEquals(value, agentConfigs.getGranularity());

    agentConfigs = new AgentConfigs();
    agentConfigs.setGranularity(value);
    assertEquals(value, agentConfigs.getGranularity());

    agentConfigs = new AgentConfigs();
    agentConfigs.setGranularity(value.toString());
    assertEquals(value, agentConfigs.getGranularity());
  }

  @Test
  public void testInclPublicMethods() {
    String key = AgentConfigs.INCLPUBLICMETHODS_KEY;
    Boolean value = false;
    AgentConfigs agentConfigs = new AgentConfigs(key + "=" + value);
    assertEquals(value, agentConfigs.getInclPublicMethods());

    agentConfigs = new AgentConfigs();
    agentConfigs.setInclPublicMethods(value);
    assertEquals(value, agentConfigs.getInclPublicMethods());
  }

  @Test
  public void testVMArgument() {
    AgentConfigs agentConfigs = new AgentConfigs();
    assertEquals("-javaagent:/tmp/foo=", agentConfigs.getVMArgument(new File("/tmp/foo")));
  }

  @Test
  public void testQuotedVMArgument() {
    AgentConfigs agentConfigs = new AgentConfigs();
    assertEquals("-javaagent:/tmp/foo=", agentConfigs.getQuotedVMArgument(new File("/tmp/foo")));
  }

  @Test
  public void testPrependNullVMArguments() {
    AgentConfigs agentConfigs = new AgentConfigs();
    assertEquals("-javaagent:/tmp/foo=",
        agentConfigs.prependVMArguments(null, new File("/tmp/foo")));
  }

  @Test
  public void testNoPrependAnyVMArguments() {
    AgentConfigs agentConfigs = new AgentConfigs();
    assertEquals("-javaagent:/tmp/foo=", agentConfigs.prependVMArguments("", new File("/tmp/foo")));
  }

  @Test
  public void testPrependVMArguments() {
    AgentConfigs agentConfigs = new AgentConfigs();
    assertEquals("-javaagent:/tmp/foo=",
        agentConfigs.prependVMArguments("-javaagent:/tmp/foo=", new File("/tmp/foo")));
    assertEquals("-javaagent:/tmp/foo= -cp .:/tmp/",
        agentConfigs.prependVMArguments("-cp .:/tmp/", new File("/tmp/foo")));
  }

  @Test
  public void testEmptyAgentConfigToString() {
    AgentConfigs agentConfigs = new AgentConfigs();
    assertTrue(agentConfigs.toString().isEmpty());
  }

  @Test
  public void testMoreThanOneAgentConfigToString() {
    String config =
        AgentConfigs.BUILDLOCATION_KEY + "=foo," + AgentConfigs.INCLPUBLICMETHODS_KEY + "=false";
    AgentConfigs agentConfigs = new AgentConfigs(config);
    assertFalse(agentConfigs.toString().isEmpty());
    assertEquals(config, agentConfigs.toString());
  }

}
