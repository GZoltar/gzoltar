package com.gzoltar.core.instr.pass;

import com.gzoltar.core.runtime.Collector;

public class OfflineInitMethodInstrumentationPass extends AbstractInitMethodInstrumentationPass {

  private static final String call =
      Collector.class.getCanonicalName() + ".instance().getHitArray(" + ARRAY_OBJECT_NAME + "); ";

  public OfflineInitMethodInstrumentationPass() {
    this.collectorCall = call;
  }

}
