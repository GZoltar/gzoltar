package com.gzoltar.core.model;

public enum NodeType {

  /**
   * 
   */
  PACKAGE("."),

  /**
   * 
   */
  CLASS("$"),

  /**
   * 
   */
  METHOD("#"),

  /**
   * 
   */
  LINE(":");

  private final String symbol;

  private NodeType(final String symbol) {
    this.symbol = symbol;
  }

  public String getSymbol() {
    return symbol;
  }

}
