package com.gzoltar.report.fl.formatter.html;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import org.apache.commons.io.IOUtils;
import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.NodeFactory;
import com.gzoltar.core.model.Tree;
import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.fl.IFormula;
import com.gzoltar.report.fl.formatter.IFaultLocalizationReportFormatter;

public abstract class AbstractHTMLReport implements IFaultLocalizationReportFormatter {

  private static String HTML_DIR = "html_views";

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
    Tree tree = this.createTree(spectrum);
    Node root = tree.getRoot();
    assert root != null;
    return this.toJSON(root, formula, tree.getNumberOfNodes()).toString();
  }

  private Tree createTree(final ISpectrum spectrum) {
    Tree tree = new Tree();
    for (Node node : spectrum.getNodes()) {
      NodeFactory.createNode(tree, node);
    }
    return tree;
  }

  private StringBuilder toJSON(final Node node, IFormula formula, int totalNumberOfNodes) {
    StringBuilder str = new StringBuilder("{");

    List<Node> children = node.getChildren();
    if (children.isEmpty()) {
      str.append("\"children\":[],");
    } else {
      str.append("\"children\":[");
      for (int i = 0; i < children.size(); i++) {
        str.append(this.toJSON(children.get(i), formula, totalNumberOfNodes));

        // is not it the last child?
        if (i < children.size() - 1) {
          str.append(",");
        }
      }
      str.append("],");
    }
    str.append("\"label\":\"" + node.getName() + "\",");
    str.append("\"probability\":" + node.getSuspiciousnessValue(formula.getName()) + ",");
    str.append("\"size\":" + totalNumberOfNodes);

    str.append("}");
    return str;
  }
}
