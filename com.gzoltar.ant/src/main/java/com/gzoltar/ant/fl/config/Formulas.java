package com.gzoltar.ant.fl.config;

import java.util.ArrayList;
import java.util.List;
import org.apache.tools.ant.types.resources.Union;

public class Formulas extends Union {

  private final List<FormulaElement> formulas = new ArrayList<FormulaElement>();

  /**
   * 
   * @return
   */
  public FormulaElement createFormula() {
    final FormulaElement formula = new FormulaElement();
    this.formulas.add(formula);
    return formula;
  }

  /**
   * 
   * @return
   */
  public List<FormulaElement> getFormulas() {
    return this.formulas;
  }

  /**
   * 
   * @return
   */
  public List<String> getNameOfFormulas() {
    List<String> formulasNames = new ArrayList<String>();
    for (FormulaElement formula : this.formulas) {
      formulasNames.add(formula.getName());
    }
    return formulasNames;
  }

  /**
   * 
   */
  public class FormulaElement {

    private String name;

    /**
     * 
     * @param name
     */
    public void setName(final String name) {
      this.name = name;
    }

    /**
     * 
     * @return
     */
    public String getName() {
      return this.name;
    }
  }
}
