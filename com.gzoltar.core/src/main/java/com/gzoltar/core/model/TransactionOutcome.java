package com.gzoltar.core.model;

public enum TransactionOutcome {

  /**
   * 
   */
  PASS("+"),

  /**
   * 
   */
  FAIL("-");

  private final String symbol;

  private TransactionOutcome(final String symbol) {
    this.symbol = symbol;
  }

  public String getSymbol() {
    return this.symbol;
  }
}
