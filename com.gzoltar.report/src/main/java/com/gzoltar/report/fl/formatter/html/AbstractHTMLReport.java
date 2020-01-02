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
