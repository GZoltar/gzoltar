package com.gzoltar.core.instr.filter;

import com.gzoltar.core.instr.actions.BlackList;
import com.gzoltar.core.instr.matchers.DuplicateCollectorReferenceMatcher;

/**
 * Filter classes that have been instrumented.
 */
public class DuplicateCollectorReferenceFilter extends Filter {

  public DuplicateCollectorReferenceFilter() {
    this.add(new BlackList(new DuplicateCollectorReferenceMatcher()));
  }

}
