package com.gzoltar.sfl.formulas;

import com.gzoltar.fl.IFormula;

public interface ISFLFormula extends IFormula {

  /**
   * Returns a suspiciousness value
   * 
   * @param n00 number of passing test cases that do not execute the faulty node
   * @param n01 number of failing test cases that do not execute the faulty node
   * @param n10 number of passing test cases that execute the faulty node
   * @param n11 number of failing test cases that execute the faulty node
   * @return
   */
  public double compute(final double n00, final double n01, final double n10, final double n11);
}
