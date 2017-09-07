package com.gzoltar.core.instrumentation.granularity;

import java.util.LinkedList;
import java.util.Queue;
import javassist.CtClass;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.analysis.ControlFlow;
import javassist.bytecode.analysis.ControlFlow.Block;

public class BasicBlockGranularity extends AbstractGranularity {

  private Queue<Integer> blocks = new LinkedList<Integer>();

  public BasicBlockGranularity(CtClass c, MethodInfo mi, CodeIterator ci) {
    super(c, mi, ci);
    try {
      ControlFlow cf = new ControlFlow(c, mi);
      for (Block block : cf.basicBlocks()) {
        blocks.add(block.position());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public boolean instrumentAtIndex(int index, int instrumentationSize) {
    boolean outcome = !blocks.isEmpty() && index >= instrumentationSize + blocks.peek();
    if (outcome)
      blocks.poll();
    return outcome;
  }

  @Override
  public boolean stopInstrumenting() {
    return blocks.isEmpty();
  }
}
