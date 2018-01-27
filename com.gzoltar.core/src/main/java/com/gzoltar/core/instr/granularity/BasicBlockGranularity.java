package com.gzoltar.core.instr.granularity;

import java.util.LinkedList;
import java.util.Queue;
import javassist.CtClass;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.analysis.ControlFlow;
import javassist.bytecode.analysis.ControlFlow.Block;

public class BasicBlockGranularity extends AbstractGranularity {

  private Queue<Integer> blocks = new LinkedList<Integer>();

  public BasicBlockGranularity(final CtClass ctClass, final MethodInfo methodInfo) {
    super(ctClass, methodInfo);
    try {
      ControlFlow cf = new ControlFlow(ctClass, methodInfo);
      for (Block block : cf.basicBlocks()) {
        this.blocks.add(block.position());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public boolean instrumentAtIndex(final int index, final int instrumentationSize) {
    boolean outcome = !this.blocks.isEmpty() && index >= instrumentationSize + this.blocks.peek();
    if (outcome) {
      this.blocks.poll();
    }
    return outcome;
  }

  @Override
  public boolean stopInstrumenting() {
    return this.blocks.isEmpty();
  }

}
