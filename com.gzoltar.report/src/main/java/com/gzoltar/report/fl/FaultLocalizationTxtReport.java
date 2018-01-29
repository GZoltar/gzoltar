package com.gzoltar.report.fl;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.model.TransactionOutcome;
import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.report.AbstractReport;

public class FaultLocalizationTxtReport extends AbstractReport {

  private final static String MATRIX_FILE_NAME = "matrix";

  private final static String SPECTRA_FILE_NAME = "spectra";

  private final static String TESTS_FILES_NAME = "tests";

  public FaultLocalizationTxtReport(final File outputDirectory) {
    super(outputDirectory);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void generateReport(final ISpectrum spectrum) throws IOException {
    if (!this.outputDirectory.exists()) {
      this.outputDirectory.mkdirs();
    }

    /**
     * Print 'matrix'
     */

    PrintWriter matrixWriter =
        new PrintWriter(this.outputDirectory + File.separator + MATRIX_FILE_NAME, "UTF-8");

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
        new PrintWriter(this.outputDirectory + File.separator + SPECTRA_FILE_NAME, "UTF-8");

    for (Node node : spectrum.getTargetNodes()) {
      spectraWriter.println(node.getNameWithLineNumber());
    }

    spectraWriter.close();

    /**
     * Print 'tests'
     */

    PrintWriter testsWriter =
        new PrintWriter(this.outputDirectory + File.separator + TESTS_FILES_NAME, "UTF-8");

    // header
    testsWriter.println("name,outcome,runtime,stacktrace");

    // content
    for (Transaction transaction : spectrum.getTransactions()) {
      testsWriter.println(transaction.getName() + ","
          + (transaction.hasFailed() ? TransactionOutcome.FAIL.name()
              : TransactionOutcome.PASS.name())
          + "," + transaction.getRuntime() + "," + transaction.getNormalizedStackTrace());
    }

    testsWriter.close();
  }
}
