package com.gzoltar.core.instr.granularity;

public interface IGranularity {

  /**
   * 
   * @param index
   * @param instrumentationSize
   * @return
   */
  public boolean instrumentAtIndex(final int index, final int instrumentationSize);

  /**
   * 
   * @return
   */
  public boolean stopInstrumenting();

}
