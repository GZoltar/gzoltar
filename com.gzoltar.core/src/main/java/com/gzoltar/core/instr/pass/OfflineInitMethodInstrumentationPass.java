package com.gzoltar.core.instr.pass;

public class OfflineInitMethodInstrumentationPass extends AbstractInitMethodInstrumentationPass {

  public OfflineInitMethodInstrumentationPass() {
    this.collectorCall =
        "com.gzoltar.internal.agent.rt.Offline.getHitArray(" + ARRAY_OBJECT_NAME + "); ";
  }

}
