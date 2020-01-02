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
