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
package com.gzoltar.systemtest;

import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.Timer;
import java.util.TimerTask;

import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.instr.InstrumentationLevel;
import com.gzoltar.core.util.SystemProperties;
import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.core.spectrum.SpectrumReader;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestName;

/**
 * SystemTestBase class that handles common tasks performed in each system test.
 */
public abstract class SystemTestBase {

  @Rule
  public TestName name = new TestName();

  @BeforeClass
  public static void checkEnv() {
    // TODO is the `mvn` command available?
  }

  @Before
  public void before() {
    String name = this.getClass().getName();
    assertTrue("Invalid name for system test: " + name, name.endsWith("SystemTest"));

    // TODO do we need to run any other procedure before the execution of each test?
  }

  @After
  public void after() {
    // TODO do we need to run any procedure after the execution of each test?
  }

  /**
   * Runs a specified `command` on a given `path`.
   */
  protected static boolean runCommand(final File path, final String command) throws Exception {
    List<String> commandLineArgs = new ArrayList<String>();
    if (SystemProperties.OS_NAME.contains("windows") == true) {
      commandLineArgs.add("cmd.exe");
      commandLineArgs.add("/c");
    } else {
      commandLineArgs.add("/bin/bash");
      commandLineArgs.add("-c");
    }
    assert commandLineArgs.size() != 0;
    commandLineArgs.add(command);

    ProcessBuilder pb = new ProcessBuilder(commandLineArgs);
    pb.redirectErrorStream(true);
    pb.inheritIO();
    pb.directory(path);
    final Process p = pb.start();

    InputStream is = p.getInputStream();
    BufferedInputStream isl = new BufferedInputStream(is);
    byte buffer[] = new byte[1024];
    int len = 0;

    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        p.destroy();
        System.err.println("A timeout has occured while running " + command);
      }
    }, 30 * 1000); // 30 seconds

    while ((len = isl.read(buffer)) != -1) {
      System.out.write(buffer, 0, len);
    }

    int exitCode = p.waitFor();
    if (timer != null) {
      timer.cancel();
    }
    assert exitCode == 0;

    return true;
  }

  /**
   * Loads a gzoltar.ser file.
   */
  protected static ISpectrum loadGZoltarSerFile(final String buildLocation, final File path) throws Exception {
    if (!path.exists() || !path.isFile()) {
      throw new RuntimeException(path + " does not exist or it is not a file!");
    }

    AgentConfigs agentConfigs = new AgentConfigs();
    agentConfigs.setInstrumentationLevel(InstrumentationLevel.NONE);
    // TODO do we need to set any other configuration?

    FileInputStream inStream = new FileInputStream(path);
    SpectrumReader spectrumReader = new SpectrumReader(buildLocation, agentConfigs, inStream);
    spectrumReader.read();
    ISpectrum spectrum = spectrumReader.getSpectrum();
    assert spectrum != null;

    return spectrum;
  }

  /**
   * Loads the target/site/gzoltar/sfl/txt/tests.csv file which follows the following
   * name,outcome,runtime,stacktrace
   */
  protected Map<String, Boolean> loadTestsCsv(final File path) throws Exception {
    if (!path.exists() || !path.isFile()) {
      throw new RuntimeException(path + " does not exist or it is not a file!");
    }

    Map<String, Boolean> tests = new LinkedHashMap<String, Boolean>();
    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
      br.readLine(); // skip header

      String row;
      while ((row = br.readLine()) != null) {
        String[] split = row.split(",");
        assert split.length == 3 || split.length == 4;
        tests.put(split[0], "PASS".equals(split[1]) ? true : false);
      }
    }

    return tests;
  }

  /**
   * Loads the target/site/gzoltar/sfl/txt/spectra.csv file which follows the following
   * name
   */
  protected Set<String> loadSpectraCsv(final File path) throws Exception {
    if (!path.exists() || !path.isFile()) {
      throw new RuntimeException(path + " does not exist or it is not a file!");
    }

    Set<String> lines = new LinkedHashSet<String>();
    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
      br.readLine(); // skip header

      String row;
      while ((row = br.readLine()) != null) {
        lines.add(row);
      }
    }

    return lines;
  }

  /**
   * Loads the target/site/gzoltar/sfl/txt/matrix.txt file.
   */
  protected List<List<Boolean>> loadMatrixTxt(final File path) throws Exception {
    if (!path.exists() || !path.isFile()) {
      throw new RuntimeException(path + " does not exist or it is not a file!");
    }

    List<List<Boolean>> matrix = new ArrayList<List<Boolean>>();
    int i = 0;
    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
      String row;
      while ((row = br.readLine()) != null) {
        int j = 0;
        matrix.add(i, new ArrayList<Boolean>());
        for (String elem : row.split(" ")) {
          matrix.get(i).add(j, ("1".equals(elem) || "-".equals(elem)) ? true : false);
          j++;
        }
      }
      i++;
    }

    return matrix;
  }

  /**
   * Loads the target/site/gzoltar/sfl/txt/ochiai.ranking.csv file which follows the following
   * name;suspiciousness_value
   */
  protected Map<String, Double> loadRankingCsv(final File path) throws Exception {
    if (!path.exists() || !path.isFile()) {
      throw new RuntimeException(path + " does not exist or it is not a file!");
    }

    Map<String, Double> ranking = new LinkedHashMap<String, Double>();
    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
      br.readLine(); // skip header

      String row;
      while ((row = br.readLine()) != null) {
        String[] split = row.split(";");
        assert split.length == 2;
        ranking.put(split[0], Double.valueOf(split[1]));
      }
    }

    return ranking;
  }

}
