package com.gzoltar.report.fl.formatter.txt;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.model.TransactionOutcome;
import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.fl.IFormula;
import com.gzoltar.report.fl.formatter.IFaultLocalizationReportFormatter;

public class FaultLocalizationTxtReport implements IFaultLocalizationReportFormatter {

  private final static String MATRIX_FILE_NAME = "matrix.txt";

  private final static String SPECTRA_FILE_NAME = "spectra.csv";

  private final static String RANKING_EXTENSION_NAME = ".ranking.csv";

  private final static String TESTS_FILES_NAME = "tests.csv";

  /**
   * {@inheritDoc}
   */
  @Override
  public void generateFaultLocalizationReport(final File outputDirectory, final ISpectrum spectrum,
      final List<IFormula> formulas) throws IOException {
    if (!outputDirectory.exists()) {
      outputDirectory.mkdirs();
    }

    /**
     * Print 'matrix'
     */

    PrintWriter matrixWriter =
        new PrintWriter(outputDirectory + File.separator + MATRIX_FILE_NAME, "UTF-8");

    for (Transaction transaction : spectrum.getTransactions()) {
      StringBuilder transactionStr = new StringBuilder();
      for (Node node : spectrum.getTargetNodes()) {
        if (transaction.isNodeActived(node)) {
          transactionStr.append("1 ");
        } else {
          transactionStr.append("0 ");
        }
      }

      if (transaction.hasFailed()) {
        transactionStr.append(TransactionOutcome.FAIL.getSymbol());
      } else {
        transactionStr.append(TransactionOutcome.PASS.getSymbol());
      }

      matrixWriter.println(transactionStr.toString());
    }

    matrixWriter.close();

    /**
     * Print 'spectra'
     */

    PrintWriter spectraWriter =
        new PrintWriter(outputDirectory + File.separator + SPECTRA_FILE_NAME, "UTF-8");

    // header
    spectraWriter.println("name");

    // content
    for (Node node : spectrum.getTargetNodes()) {
      spectraWriter.println(node.getNameWithLineNumber());
    }

    spectraWriter.close();

    /**
     * Print a ranking file per formula
     */

    List<Node> nodes = new ArrayList<Node>(spectrum.getTargetNodes());
    for (final IFormula formula : formulas) {

      PrintWriter formulaWriter = new PrintWriter(outputDirectory + File.separator
          + formula.getName().toLowerCase() + RANKING_EXTENSION_NAME, "UTF-8");

      // header
      formulaWriter.println("name;suspiciousness_value");

      // sort (DESC) nodes by their suspiciousness value
      Collections.sort(nodes, new Comparator<Node>() {
        @Override
        public int compare(Node node0, Node node1) {
          return Double.compare(node1.getSuspiciousnessValue(formula.getName()),
              node0.getSuspiciousnessValue(formula.getName()));
        }
      });

      for (Node node : nodes) {
        formulaWriter.println(
            node.getNameWithLineNumber() + ";" + node.getSuspiciousnessValue(formula.getName()));
      }

      formulaWriter.close();
    }

    /**
     * Print 'tests'
     */

    PrintWriter testsWriter =
        new PrintWriter(outputDirectory + File.separator + TESTS_FILES_NAME, "UTF-8");

    // header
    testsWriter.println("name,outcome,runtime,stacktrace");

    // content
    for (Transaction transaction : spectrum.getTransactions()) {
      testsWriter.println(transaction.getName() + ","
          + (transaction.hasFailed() ? TransactionOutcome.FAIL.name()
              : TransactionOutcome.PASS.name())
          + "," + transaction.getRuntime() + "," + transaction.getStackTrace());
    }

    testsWriter.close();
  }
}
