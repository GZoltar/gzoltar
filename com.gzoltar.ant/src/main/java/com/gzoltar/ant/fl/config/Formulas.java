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
