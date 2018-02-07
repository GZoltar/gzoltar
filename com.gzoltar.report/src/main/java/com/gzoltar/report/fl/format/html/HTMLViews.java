package com.gzoltar.report.fl.format.html;

public enum HTMLViews {

  /** */
  BUBBLE_HIERARCHY("bubblehierarchy.html"),

  /** */
  SUNBURST("sunburst.html"),

  /** */
  VERTICAL_PARTITION("verticalpartition.html");

  private final String fileName;

  private HTMLViews(final String fileName) {
    this.fileName = fileName;
  }

  public String getFileName() {
    return fileName;
  }
}
