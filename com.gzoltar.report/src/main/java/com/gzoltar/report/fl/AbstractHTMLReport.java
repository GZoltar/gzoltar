package com.gzoltar.report.fl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import org.apache.commons.io.IOUtils;
import com.gzoltar.core.model.Node;
import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.fl.IFormula;
import com.gzoltar.report.AbstractReport;

public abstract class AbstractHTMLReport extends AbstractReport {

  private static String HTML_DIR = "html_views";

  protected AbstractHTMLReport(final File outputDirectory, final List<IFormula> formulas) {
    super(outputDirectory, formulas);
  }

  /**
   * 
   * @param fileName
   * @return
   */
  protected String readFile(final String fileName) {
    try {
      InputStream is = FaultLocalizationHTMLReport.class
          .getResourceAsStream(File.separator + HTML_DIR + File.separator + fileName);
      return IOUtils.toString(is, Charset.defaultCharset());
    } catch (IOException e) {
      e.printStackTrace();
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 
   * @param spectrum
   * @return
   */
  protected String toJSON(final ISpectrum spectrum, final IFormula formula) {
    Node root = spectrum.getTree().getRoot();
    assert root != null;
    return this.toJSON(root, formula).toString();
  }

  private StringBuilder toJSON(final Node node, IFormula formula) {
    StringBuilder str = new StringBuilder("{");

    List<Node> children = node.getChildren();
    if (children.isEmpty()) {
      str.append("\"children\":[],");
    } else {
      str.append("\"children\":[");
      for (int i = 0; i < children.size(); i++) {
        str.append(this.toJSON(children.get(i), formula));

        // is not it the last child?
        if (i < children.size() - 1) {
          str.append(",");
        }
      }
      str.append("],");
    }
    str.append("\"label\":\"" + node.getName() + "\",");
    str.append("\"probability\":" + node.getSuspiciousnessValue(formula.getName()) + ",");
    str.append("\"size\":26"); // FIXME

    str.append("}");
    return str;
  }
}
