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
package com.gzoltar.core.instr.pass;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.instr.InstrumentationConstants;
import com.gzoltar.core.instr.InstrumentationLevel;
import com.gzoltar.core.instr.Outcome;
import com.gzoltar.core.instr.actions.AnonymousClassConstructorFilter;
import com.gzoltar.core.instr.filter.EmptyMethodFilter;
import com.gzoltar.core.instr.filter.EnumFilter;
import com.gzoltar.core.instr.filter.IFilter;
import com.gzoltar.core.instr.filter.SyntheticFilter;
import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.NodeFactory;
import com.gzoltar.core.runtime.Collector;
import com.gzoltar.core.runtime.Probe;
import com.gzoltar.core.runtime.ProbeGroup;
import com.gzoltar.core.util.MD5;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.bytecode.Bytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.Opcode;
import javassist.bytecode.analysis.ControlFlow;
import javassist.bytecode.analysis.ControlFlow.Block;

public class CoveragePass implements IPass {

  private final InstrumentationLevel instrumentationLevel;

  private final FieldPass fieldPass = new FieldPass();

  private AbstractInitMethodPass initMethodPass = null;

  private final StackSizePass stackSizePass = new StackSizePass();

  private final List<IFilter> filters = new ArrayList<IFilter>();

  private ProbeGroup probeGroup;

  public CoveragePass(final AgentConfigs agentConfigs) {

    this.instrumentationLevel = agentConfigs.getInstrumentationLevel();
    switch (this.instrumentationLevel) {
      case FULL:
      default:
        this.initMethodPass = new InitMethodPass();
        break;
      case OFFLINE:
        this.initMethodPass = new OfflineInitMethodPass();
        break;
      case NONE:
        break;
    }

    // exclude synthetic methods
    this.filters.add(new SyntheticFilter());

    // exclude methods 'values' and 'valuesOf' of enum classes
    this.filters.add(new EnumFilter());

    // exclude methods without any source code
    this.filters.add(new EmptyMethodFilter());

    // exclude constructor of an Anonymous class as the same line number is handled by the
    // superclass
    this.filters.add(new AnonymousClassConstructorFilter());
  }

  @Override
  public synchronized Outcome transform(final CtClass ctClass) throws Exception {
    boolean instrumented = false;

    byte[] originalBytes = ctClass.toBytecode(); // toBytecode() method frozens the class
    // in order to be able to modify it, it has to be defrosted
    ctClass.defrost();

    String hash = MD5.calculateHash(originalBytes);
    this.probeGroup = new ProbeGroup(hash, ctClass);

    for (CtBehavior ctBehavior : ctClass.getDeclaredBehaviors()) {
      boolean behaviorInstrumented =
          this.transform(ctClass, ctBehavior).equals(Outcome.REJECT) ? false : true;
      instrumented = instrumented || behaviorInstrumented;

      if (behaviorInstrumented) {
        // update stack size
        this.stackSizePass.transform(ctClass, ctBehavior);
      }
    }

    // register class' probes
    Collector.instance().regiterProbeGroup(this.probeGroup);

    if (instrumented && this.initMethodPass != null) {
      // make GZoltar's field
      this.fieldPass.transform(ctClass);

      // make method to init GZoltar's field
      this.initMethodPass.setHash(hash);
      this.initMethodPass.transform(ctClass);

      // make sure GZoltar's field is initialised. note: the following code requires the init method
      // to be in the instrumented class, otherwise a compilation error is thrown

      boolean hasAnyStaticInitializerBeenInstrumented = false;
      for (CtBehavior ctBehavior : ctClass.getDeclaredBehaviors()) {
        if (ctBehavior.getName().equals(InstrumentationConstants.INIT_METHOD_NAME)) {
          // for obvious reasons, init method cannot call itself
          continue;
        }

        // before executing the code of every single method, check whether FIELD_NAME has been
        // initialised. if not, init method should initialise the field
        this.initMethodPass.transform(ctClass, ctBehavior);

        if (hasAnyStaticInitializerBeenInstrumented == false
            && ctBehavior.getMethodInfo2().isStaticInitializer()) {
          hasAnyStaticInitializerBeenInstrumented = true;
        }
      }

      if (!hasAnyStaticInitializerBeenInstrumented) {
        CtConstructor clinit = ctClass.makeClassInitializer();
        this.initMethodPass.transform(ctClass, clinit);
      }
    }

    return Outcome.ACCEPT;
  }

  @Override
  public Outcome transform(final CtClass ctClass, final CtBehavior ctBehavior) throws Exception {
    Outcome instrumented = Outcome.REJECT;

    // check whether this method should be instrumented
    for (IFilter filter : this.filters) {
      switch (filter.filter(ctBehavior)) {
        case REJECT:
          return instrumented;
        case ACCEPT:
        default:
          continue;
      }
    }

    boolean injectBytecode = this.instrumentationLevel == InstrumentationLevel.FULL
        || this.instrumentationLevel == InstrumentationLevel.OFFLINE;

    MethodInfo methodInfo = ctBehavior.getMethodInfo();
    CodeAttribute ca = methodInfo.getCodeAttribute();

    assert ca != null;
    CodeIterator ci = ca.iterator();

    Queue<Integer> blocks = new LinkedList<Integer>();
    try {
      ControlFlow cf = new ControlFlow(ctClass, methodInfo);
      for (Block block : cf.basicBlocks()) {
        blocks.add(block.position());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    int index = 0, prevLine = -1, curLine = -1, instrSize = 0;
    while (ci.hasNext()) {
      index = ci.next();
      curLine = methodInfo.getLineNumber(index);

      if (curLine == -1) {
        continue;
      }

      boolean isNewBlock = !blocks.isEmpty() && index >= instrSize + blocks.peek();
      if (isNewBlock) {
        blocks.poll();
      }

      if (prevLine != curLine || isNewBlock) {
        // a line is always considered for instrumentation if and only if: 1) it's line number has
        // not been instrumented; 2) or, if it's in a different block

        Node node = NodeFactory.createNode(ctClass, ctBehavior, curLine, isNewBlock);
        assert node != null;
        Probe probe = this.probeGroup.registerProbe(node, ctBehavior);
        assert probe != null;

        if (injectBytecode) {
          Bytecode bc = this.getInstrumentationCode(ctClass, probe, methodInfo.getConstPool());
          ci.insert(index, bc.get());
          instrSize += bc.length();
          instrumented = Outcome.ACCEPT;
        } else {
          instrumented = Outcome.REJECT;
        }

        prevLine = curLine;
      }
    }

    return instrumented;
  }

  private Bytecode getInstrumentationCode(CtClass ctClass, Probe probe, ConstPool constPool) {
    Bytecode b = new Bytecode(constPool);
    b.addGetstatic(ctClass, InstrumentationConstants.FIELD_NAME,
        InstrumentationConstants.FIELD_DESC_BYTECODE);
    b.addIconst(probe.getArrayIndex());
    b.addOpcode(Opcode.ICONST_1);
    b.addOpcode(Opcode.BASTORE);

    return b;
  }

}
