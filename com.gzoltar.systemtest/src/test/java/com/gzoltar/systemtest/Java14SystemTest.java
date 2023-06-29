package com.gzoltar.systemtest;

import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.core.util.SystemProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class Java14SystemTest extends SystemTestBase{

    private static final String PROJECT_ROOT_DIR_PATH = "target" + SystemProperties.FILE_SEPARATOR +
            "projects" + SystemProperties.FILE_SEPARATOR +
            "examples" + SystemProperties.FILE_SEPARATOR +
            "java-14";

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
        assertEquals(15, SPECTRUM.getNumberOfNodes());
    }

    @Test
    public void testSetOfNodes() {
        // Does the spectrum object contain some of the expected set of instrumented lines of code?
        List<Node> nodes = SPECTRUM.getNodes();
        Set<String> lines = new LinkedHashSet<String>() { {
            add("org.gzoltar.examples$WeekDay#WeekDay():26");
            add("org.gzoltar.examples$WeekDay#shortToLong(java.lang.String):31");
            add("org.gzoltar.examples$WeekDay#shortToLong(java.lang.String):38");
            add("org.gzoltar.examples$WeekDay#WeekDay():27");
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
        assertEquals(8, SPECTRUM.getNumberOfTransactions());
    }

    @Test
    public void testSetOfTransactions() {
        // Does the spectrum object contain some of the expected transactions?
        List<Transaction> transactions = SPECTRUM.getTransactions();
        Set<String> someTransactionsNames = new LinkedHashSet<String>() { {
            add("org.gzoltar.examples.WeekDayTest#test1");
        }
        };
        Iterator<String> it = someTransactionsNames.iterator();
        while (it.hasNext()) {
            String transactionName = it.next();
            for (Transaction transaction : transactions) {
                System.out.println(transaction.getName());
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
        assertEquals(1, numFail);
    }

    @Test
    public void testSetOfFailingTransactions() {
        // Do the set of expected failing tests fail?
        List<Transaction> transactions = SPECTRUM.getTransactions();
        Set<String> failingTransactionsNames = new LinkedHashSet<String>() { {
            add("org.gzoltar.examples.WeekDayTest#test4");
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

    @Test
    public void testCoverage() {
        // Is the coverage of tests X, Y, ... correctly reported in the target/gzoltar.ser file?

        // org.gzoltar.examples.StaticFieldTest#test1 and org.gzoltar.examples.StaticFieldTest#test3 must have the exact same activity
        Transaction a = SPECTRUM.findTransactionByName("org.gzoltar.examples.StaticFieldTest#test1");
        Transaction b = SPECTRUM.findTransactionByName("org.gzoltar.examples.StaticFieldTest#test2");
        Transaction c = SPECTRUM.findTransactionByName("org.gzoltar.examples.StaticFieldTest#test3");
        EqualsBuilder builder = new EqualsBuilder();
        Map<String, Pair<String, boolean[]>> da = a.getActivity();
        Map<String, Pair<String, boolean[]>> db = a.getActivity();
        builder.reflectionAppend(da, db);
        assertTrue(builder.isEquals());
        fail();

        // org.gzoltar.examples.StaticFieldTest#test3 must cover the `clinit` constructor, i.e.,
        // org.gzoltar.examples$StaticField#<clinit>():24
        // org.gzoltar.examples$StaticField#<clinit>():27
        // org.gzoltar.examples$StaticField#<clinit>():28
        // and the `getFoo` method, i.e.,
        // org.gzoltar.examples$StaticField#getFoo():31
        List<Node> cHitNodes = SPECTRUM.getHitNodes(c);
        assertEquals(4, cHitNodes.size());
        Set<String> cHitNodesNames = new LinkedHashSet<String>() { {
            add("org.gzoltar.examples$StaticField#<clinit>():24");
            add("org.gzoltar.examples$StaticField#<clinit>():27");
            add("org.gzoltar.examples$StaticField#<clinit>():28");
            add("org.gzoltar.examples$StaticField#getFoo():31");
        }
        };
        Iterator<String> cit = cHitNodesNames.iterator();
        while (cit.hasNext()) {
            String hitNodeName = cit.next();
            for (Node node : cHitNodes) {
                if (hitNodeName.equals(node.getName())) {
                    cit.remove();
                    break;
                }
            }
        }
        assertEquals(0, cHitNodesNames.size());

        // org.gzoltar.examples.StaticFieldTest#test2 activity must be empty
        List<Node> bHitNodes = SPECTRUM.getHitNodes(b);
        assertEquals(0, bHitNodes.size());

        // org.gzoltar.examples.WeekDayTest#test1 traditional test
        Transaction d = SPECTRUM.findTransactionByName("org.gzoltar.examples.WeekDayTest#test1");
        List<Node> dHitNodes = SPECTRUM.getHitNodes(d);
        assertEquals(6, dHitNodes.size());
        Set<String> dHitNodesNames = new LinkedHashSet<String>() { {
            add("org.gzoltar.examples$WeekDay#WeekDay():26");
            add("org.gzoltar.examples$WeekDay#WeekDay():27");
            add("org.gzoltar.examples$WeekDay#WeekDay():28");
            add("org.gzoltar.examples$WeekDay#shortToLong(java.lang.String):31");
            add("org.gzoltar.examples$WeekDay#shortToLong(java.lang.String):37");
            add("org.gzoltar.examples$WeekDay#shortToLong(java.lang.String):39");
        }
        };
        Iterator<String> dit = dHitNodesNames.iterator();
        while (dit.hasNext()) {
            String hitNodeName = dit.next();
            for (Node node : dHitNodes) {
                if (hitNodeName.equals(node.getName())) {
                    dit.remove();
                    break;
                }
            }
        }
        assertEquals(0, dHitNodesNames.size());
    }

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
        assertEquals(8, tests.size());
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
            add("org.gzoltar.examples.WeekDayTest#test1");
            add("org.gzoltar.examples.WeekDayTest#test6");
            add("org.gzoltar.examples.WeekDayTest#test8");
        }
        };
        for (String testName : someTestsNames) {
            assertTrue(tests.containsKey(testName));
        }
        assertFalse(tests.containsKey("org.gzoltar.examples.WeekDayTest#test85"));
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
        assertEquals(1, numFailingTests);

        Set<String> failingTestsNames = new LinkedHashSet<String>() { {
            add("org.gzoltar.examples.WeekDayTest#test4");
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
        assertEquals(15, lines.size());

        Set<String> someLinesNames = new LinkedHashSet<String>() { {
            add("org.gzoltar.examples$WeekDay#getDummy():50");
            add("org.gzoltar.examples$WeekDay#shortToLong(java.lang.String):45");
            add("org.gzoltar.examples$WeekDay#shortToLong(java.lang.String):42");
            add("org.gzoltar.examples$WeekDay#shortToLong(java.lang.String):38");
        }
        };
        for (String lineName : someLinesNames) {
            assertTrue(lines.contains(lineName));
        }
        assertFalse(lines.contains("org.gzoltar.examples$WeekDay#shortToLong(java.lang.String):381"));
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
        assertEquals(8, matrix.size());
        assertEquals(15+1, matrix.get(0).size()); // 20 columns (aka lines of code) + 1 test outcome

        int numFailingTests = 0;
        for (int i = 0; i < matrix.size(); i++) {
            if (matrix.get(i).get(matrix.get(i).size() - 1) == true) {
                numFailingTests++;
            }
        }
        assertEquals(1, numFailingTests);

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
        assertEquals(15, ranking.size());

        // Check top-3
        Iterator<Map.Entry<String, Double>> it = ranking.entrySet().iterator();
        Map.Entry<String, Double> entry = null;

        // 1st
        entry = it.next();
        assertEquals("org.gzoltar.examples$WeekDay#shortToLong(java.lang.String):41", entry.getKey());
        assertEquals(1.0, entry.getValue(), 0.01);

        // 2nd
        entry = it.next();
        assertEquals("org.gzoltar.examples$WeekDay#WeekDay():26", entry.getKey());
        assertEquals(0.35, entry.getValue(), 0.01);

        // 3rd
        entry = it.next();
        assertEquals("org.gzoltar.examples$WeekDay#WeekDay():27", entry.getKey());
        assertEquals(0.35, entry.getValue(), 0.01);
    }

}
