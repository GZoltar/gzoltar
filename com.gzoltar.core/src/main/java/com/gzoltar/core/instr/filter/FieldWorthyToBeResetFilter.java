/**
 * Copyright (C) 2018 GZoltar contributors.
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
package com.gzoltar.core.instr.filter;

import com.gzoltar.core.instr.InstrumentationConstants;
import com.gzoltar.core.instr.actions.BlackList;
import com.gzoltar.core.instr.matchers.FieldModifierMatcher;
import com.gzoltar.core.instr.matchers.FieldNameMatcher;
import com.gzoltar.core.instr.matchers.NotMatcher;
import com.gzoltar.core.instr.matchers.OrMatcher;
import javassist.bytecode.AccessFlag;

/**
 * Filter a specific list of static fields that should not be reset every time a unit test case is
 * executed.
 */
public class FieldWorthyToBeResetFilter extends Filter {

  public FieldWorthyToBeResetFilter() {
    BlackList specificStaticFieldsFilter = new BlackList(new OrMatcher(
        // skip non static fields of being reset
        new NotMatcher(new FieldModifierMatcher(AccessFlag.STATIC)),
        // in theory the field named serialVersionUID is a constant which should not be reset in any
        // circumstances as it will break the serialization of the class
        new FieldNameMatcher("serialVersionUID"),
        // skip GZoltar fields
        new FieldNameMatcher(InstrumentationConstants.PREFIX + "*")));

    this.add(specificStaticFieldsFilter);
  }
}
