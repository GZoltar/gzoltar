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
package org.gzoltar.systemtest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.LinkedHashSet;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.tuple.Pair;

import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.core.util.SystemProperties;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * GZoltar under Java-8.
 */
public class Java8SystemTest extends SystemTestBase {

  private static final String PROJECT_ROOT_DIR_PATH = "target" + SystemProperties.FILE_SEPARATOR +
    "projects" + SystemProperties.FILE_SEPARATOR +
    "examples" + SystemProperties.FILE_SEPARATOR +
    "java-8";

  private static final File PROJECT_ROOT_DIR = new File(PROJECT_ROOT_DIR_PATH);

  private static ISpectrum SPECTRUM = null;

  @BeforeClass
  public static void runGZoltar() throws Exception {
    // Run Apache Maven on a specific project example
    // 1. Clean project
    runCommand(PROJECT_ROOT_DIR, "mvn clean test-compile");
    // 2. Run all unit tests and collect coverage
    runCommand(PROJECT_ROOT_DIR, "mvn -P custom -Dgzoltar.includes=*Test#*:*Tests#* gzoltar:list-test-methods");
    runCommand(PROJECT_ROOT_DIR, "mvn -P custom -Dgzoltar.offline=false -Dgzoltar.collectCoverage=true gzoltar:run-test-methods");
    // 3. Generate a fault localization report
    runCommand(PROJECT_ROOT_DIR, "mvn gzoltar:fl-report");

    SPECTRUM = loadGZoltarSerFile(
      PROJECT_ROOT_DIR_PATH + SystemProperties.FILE_SEPARATOR + "target" + SystemProperties.FILE_SEPARATOR + "classes",
      new File(PROJECT_ROOT_DIR_PATH + SystemProperties.FILE_SEPARATOR +
        "target" + SystemProperties.FILE_SEPARATOR +
        "gzoltar.ser")
    );
    System.out.println("[DEBUG]\n" + SPECTRUM.toString());
  }

  //
  // target/gzoltar.ser
  //

  @Test
  public void testNumberOfNodes() {
    // Does the spectrum object contain the exact number of expected set of instrumented lines of code?
    assertEquals(70, SPECTRUM.getNumberOfNodes());
  }

  @Test
  public void testSetOfNodes() {
    // Does the spectrum object contain some of the expected set of instrumented lines of code?
    List<Node> nodes = SPECTRUM.getNodes();
    Set<String> lines = new LinkedHashSet<String>() { {
        add("org.gzoltar.examples$ExampleInterface#ExampleInterface():30");
        add("org.gzoltar.examples$ExampleLambda#ExampleLambda():22");
        add("org.gzoltar.examples$ExampleStream#ExampleStream():24");
      }
    };
    Iterator<String> it = lines.iterator();
    while (it.hasNext()) {
      String line = it.next();
      for (Node node : nodes) {
        if (line.equals(node.getNameWithLineNumber())) {
          it.remove();
          break;
        }
      }
    }
    assertEquals(0, lines.size());
  }

  @Test
  public void testNumberOfTransactions() {
    // Does the spectrum object contain the exact number of expected transactions?
    assertEquals(16, SPECTRUM.getNumberOfTransactions());
  }

  @Test
  public void testSetOfTransactions() {
    // Does the spectrum object contain some of the expected transactions?
    List<Transaction> transactions = SPECTRUM.getTransactions();
    Set<String> someTransactionsNames = new LinkedHashSet<String>() { {
        add("org.gzoltar.examples.ExampleInterfaceTest#test1");
        add("org.gzoltar.examples.ExampleLambdaTest#test1");
        add("org.gzoltar.examples.ExampleStreamTest#test1");
      }
    };
    Iterator<String> it = someTransactionsNames.iterator();
    while (it.hasNext()) {
      String transactionName = it.next();
      for (Transaction transaction : transactions) {
        if (transactionName.equals(transaction.getName())) {
          it.remove();
          break;
        }
      }
    }
    assertEquals(0, someTransactionsNames.size());
  }

  @Test
  public void testNumFailingTransactions() {
    // Is the number of failing tests correct?
    List<Transaction> transactions = SPECTRUM.getTransactions();
    int numFail = 0;
    for (Transaction transaction : transactions) {
      if (transaction.hasFailed()) {
        numFail++;
      }
    }
    assertEquals(4, numFail);
  }

  @Test
  public void testSetOfFailingTransactions() {
    // Do the set of expected failing tests fail?
    List<Transaction> transactions = SPECTRUM.getTransactions();
    Set<String> failingTransactionsNames = new LinkedHashSet<String>() { {
        add("org.gzoltar.examples.ExampleInterfaceTest#test3");
        add("org.gzoltar.examples.ExampleLambdaTest#test4");
        add("org.gzoltar.examples.ExampleStreamTest#test1");
        add("org.gzoltar.examples.ExampleStreamTest#test4");
      }
    };
    Iterator<String> it = failingTransactionsNames.iterator();
    while (it.hasNext()) {
      String transactionName = it.next();
      for (Transaction transaction : transactions) {
        if (transactionName.equals(transaction.getName()) && transaction.hasFailed()) {
          it.remove();
          break;
        }
      }
    }
    assertEquals(0, failingTransactionsNames.size());
  }

  /* 
  @Test
  public void testCoverage() {
    //TBD
  }
  */

  //
  // target/site/gzoltar/sfl/txt/<matrix.txt|ochiai.ranking.csv|tests.csv|spectra.csv>
  //

  @Test
  public void testTestCsvNumberOfTests() throws Exception {
    // Does the tests.csv report the expected number of tests?
    Map<String, Boolean> tests = super.loadTestsCsv(
      new File(PROJECT_ROOT_DIR_PATH + SystemProperties.FILE_SEPARATOR +
        "target" + SystemProperties.FILE_SEPARATOR +
        "site" + SystemProperties.FILE_SEPARATOR +
        "gzoltar" + SystemProperties.FILE_SEPARATOR +
        "sfl" + SystemProperties.FILE_SEPARATOR +
        "txt" + SystemProperties.FILE_SEPARATOR +
        "tests.csv"
    ));
    assertEquals(16, tests.size());
  }

  @Test
  public void testTestCsvSetOfTests() throws Exception {
    // Does the tests.csv report the expected set of tests?
    Map<String, Boolean> tests = super.loadTestsCsv(
      new File(PROJECT_ROOT_DIR_PATH + SystemProperties.FILE_SEPARATOR +
        "target" + SystemProperties.FILE_SEPARATOR +
        "site" + SystemProperties.FILE_SEPARATOR +
        "gzoltar" + SystemProperties.FILE_SEPARATOR +
        "sfl" + SystemProperties.FILE_SEPARATOR +
        "txt" + SystemProperties.FILE_SEPARATOR +
        "tests.csv"
    ));

    Set<String> someTestsNames = new LinkedHashSet<String>() { {
        add("org.gzoltar.examples.ExampleInterfaceTest#test1");
        add("org.gzoltar.examples.ExampleLambdaTest#test1");
        add("org.gzoltar.examples.ExampleStreamTest#test1");
      }
    };
    for (String testName : someTestsNames) {
      assertTrue(tests.containsKey(testName));
    }
    assertFalse(tests.containsKey("org.gzoltar.examples.ExampleStreamTest#test5"));
  }

  @Test
  public void testTestCsvSetOfFailingTests() throws Exception {
    // Does the tests.csv report the expected set of failing tests?
    Map<String, Boolean> tests = super.loadTestsCsv(
      new File(PROJECT_ROOT_DIR_PATH + SystemProperties.FILE_SEPARATOR +
        "target" + SystemProperties.FILE_SEPARATOR +
        "site" + SystemProperties.FILE_SEPARATOR +
        "gzoltar" + SystemProperties.FILE_SEPARATOR +
        "sfl" + SystemProperties.FILE_SEPARATOR +
        "txt" + SystemProperties.FILE_SEPARATOR +
        "tests.csv"
    ));
    int numFailingTests = 0;
    for (Boolean testOutcome : tests.values()) {
      if (testOutcome == false) {
        numFailingTests++;
      }
    }
    assertEquals(4, numFailingTests);

    Set<String> failingTestsNames = new LinkedHashSet<String>() { {
        add("org.gzoltar.examples.ExampleInterfaceTest#test3");
        add("org.gzoltar.examples.ExampleLambdaTest#test4");
        add("org.gzoltar.examples.ExampleStreamTest#test1");
        add("org.gzoltar.examples.ExampleStreamTest#test4");
      }
    };
    for (String testName : failingTestsNames) {
      assertFalse(tests.get(testName));
    }
  }

  @Test
  public void testSpectraCsv() throws Exception {
    // Does the spectra.csv report the expected set of lines of code?
    Set<String> lines = super.loadSpectraCsv(
      new File(PROJECT_ROOT_DIR_PATH + SystemProperties.FILE_SEPARATOR +
        "target" + SystemProperties.FILE_SEPARATOR +
        "site" + SystemProperties.FILE_SEPARATOR +
        "gzoltar" + SystemProperties.FILE_SEPARATOR +
        "sfl" + SystemProperties.FILE_SEPARATOR +
        "txt" + SystemProperties.FILE_SEPARATOR +
        "spectra.csv"
    ));
    assertEquals(70, lines.size());

    Set<String> someLinesNames = new LinkedHashSet<String>() { {
        add("org.gzoltar.examples$ExampleInterface#ExampleInterface():30");
        add("org.gzoltar.examples$ExampleInterface#referingPrint():128");
        add("org.gzoltar.examples$ExampleLambda#ExampleLambda():22");
        add("org.gzoltar.examples$ExampleLambda#multiArgs():55");
        add("org.gzoltar.examples$ExampleStream#ExampleStream():24");
        add("org.gzoltar.examples$ExampleStream#printNumbers(java.util.List):39");
      }
    };
    for (String lineName : someLinesNames) {
      assertTrue(lines.contains(lineName));
    }
    assertFalse(lines.contains("org.gzoltar.examples.ExampleInterfaceTest#test1"));
  }

  @Test
  public void testMatrixTxt() throws Exception {
    // Does the matrix.txt report the expected number of rows and columns?
    List<List<Boolean>> matrix = super.loadMatrixTxt(
      new File(PROJECT_ROOT_DIR_PATH + SystemProperties.FILE_SEPARATOR +
        "target" + SystemProperties.FILE_SEPARATOR +
        "site" + SystemProperties.FILE_SEPARATOR +
        "gzoltar" + SystemProperties.FILE_SEPARATOR +
        "sfl" + SystemProperties.FILE_SEPARATOR +
        "txt" + SystemProperties.FILE_SEPARATOR +
        "matrix.txt"
    ));
    assertEquals(16, matrix.size());
    assertEquals(70+1, matrix.get(0).size()); // 20 columns (aka lines of code) + 1 test outcome

    int numFailingTests = 0;
    for (int i = 0; i < matrix.size(); i++) {
      if (matrix.get(i).get(matrix.get(i).size() - 1) == true) {
        numFailingTests++;
      }
    }
    assertEquals(4, numFailingTests);

    // TODO Check whether some specific cells are 0 (not covered) or 1 (covered)
  }

  @Test
  public void testRankingCsv() throws Exception {
    // Does the ranking.csv report the expected ranking?
    Map<String, Double> ranking = super.loadRankingCsv(
      new File(PROJECT_ROOT_DIR_PATH + SystemProperties.FILE_SEPARATOR +
        "target" + SystemProperties.FILE_SEPARATOR +
        "site" + SystemProperties.FILE_SEPARATOR +
        "gzoltar" + SystemProperties.FILE_SEPARATOR +
        "sfl" + SystemProperties.FILE_SEPARATOR +
        "txt" + SystemProperties.FILE_SEPARATOR +
        "ochiai.ranking.csv"
    ));
    assertEquals(70, ranking.size());

    // Check top-3
    Iterator<Map.Entry<String, Double>> it = ranking.entrySet().iterator();
    Map.Entry<String, Double> entry = null;

    // 1st
    entry = it.next();
    assertEquals("org.gzoltar.examples$ExampleStream#ExampleStream():24", entry.getKey());
    //assertEquals(0.81, entry.getValue(), 0.01);

    // 2nd
    entry = it.next();
    assertEquals("org.gzoltar.examples$ExampleInterface#getLength():93", entry.getKey());
    //assertEquals(0.40, entry.getValue(), 0.01);

    // 3rd
    entry = it.next();
    assertEquals("org.gzoltar.examples$ExampleInterface#getLength():96", entry.getKey());
    //assertEquals(0.40, entry.getValue(), 0.01);
  }

}
