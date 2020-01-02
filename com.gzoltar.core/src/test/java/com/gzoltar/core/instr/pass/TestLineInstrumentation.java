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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;
import org.gzoltar.examples.AbstractClass;
import org.gzoltar.examples.AnonymousClass;
import org.gzoltar.examples.DeprecatedAnnotation;
import org.gzoltar.examples.EnumClass;
import org.gzoltar.examples.InnerClass;
import org.gzoltar.examples.InterfaceClass;
import org.gzoltar.examples.PrivateModifiers;
import org.gzoltar.examples.ProtectedModifiers;
import org.gzoltar.examples.PublicFinalModifiers;
import org.gzoltar.examples.PublicModifiers;
import org.gzoltar.examples.PublicStaticModifiers;
import org.junit.Before;
import org.junit.Test;
import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.events.EmptyEventListener;
import com.gzoltar.core.instr.InstrumentationLevel;
import com.gzoltar.core.instr.granularity.GranularityLevel;
import com.gzoltar.core.model.Node;
import com.gzoltar.core.runtime.Collector;
import com.gzoltar.core.spectrum.ISpectrum;
import javassist.ClassPool;

@SuppressWarnings("deprecation")
public class TestLineInstrumentation {

  private final static ClassPool pool = ClassPool.getDefault();

  @Before
  public void beforeEachUnitTest() {
    Collector.restart();
  }

  private void test(List<String> classesUnderTest, List<Integer> lineNumbers) throws Exception {
    AgentConfigs configs = new AgentConfigs();
    configs.setGranularity(GranularityLevel.LINE);
    configs.setInstrumentationLevel(InstrumentationLevel.NONE);

    Collector.instance().addListener(new EmptyEventListener());

    CoveragePass instrumentationPass = new CoveragePass(configs);
    for (String classUnderTest : classesUnderTest) {
      instrumentationPass.transform(pool.get(classUnderTest));
    }

    ISpectrum spectrum = Collector.instance().getSpectrum();
    assertEquals(lineNumbers.size(), spectrum.getNumberOfNodes());

    if (lineNumbers.isEmpty()) {
      return;
    }

    List<Node> leafs = spectrum.getNodes();
    assertEquals(lineNumbers.size(), leafs.size());

    for (int i = 0; i < lineNumbers.size(); i++) {
      assertTrue(lineNumbers.get(i) == leafs.get(i).getLineNumber());
    }

  }

  @Test
  public void testProbesOfEnumClass() throws Exception {
    List<String> classesUnderTest = new ArrayList<String>();
    classesUnderTest.add(EnumClass.class.getCanonicalName());

    List<Integer> lineNumbers = new ArrayList<Integer>();
    lineNumbers.add(19); // "normal" constructor
    lineNumbers.add(24);
    lineNumbers.add(29);
    lineNumbers.add(34);
    lineNumbers.add(19); // clinit

    this.test(classesUnderTest, lineNumbers);
  }

  @Test
  public void testProbesOfInterfaceClass() throws Exception {
    List<String> classesUnderTest = new ArrayList<String>();
    classesUnderTest.add(InterfaceClass.class.getCanonicalName());

    this.test(classesUnderTest, new ArrayList<Integer>());
  }

  @Test
  public void testProbesOfAbstractClass() throws Exception {
    List<String> classesUnderTest = new ArrayList<String>();
    classesUnderTest.add(AbstractClass.class.getCanonicalName());

    List<Integer> lineNumbers = new ArrayList<Integer>();
    lineNumbers.add(19);
    lineNumbers.add(24);

    this.test(classesUnderTest, lineNumbers);
  }

  @Test
  public void testProbesOfAnonymousClass() throws Exception {
    List<String> classesUnderTest = new ArrayList<String>();
    classesUnderTest.add(AnonymousClass.class.getCanonicalName());
    classesUnderTest.add(AnonymousClass.class.getCanonicalName() + "$1");

    List<Integer> lineNumbers = new ArrayList<Integer>();
    // AnonymousClass
    lineNumbers.add(23);
    lineNumbers.add(26);
    lineNumbers.add(38);
    // AnonymousClass$1
    lineNumbers.add(29);
    lineNumbers.add(30);
    lineNumbers.add(31);
    lineNumbers.add(32);
    lineNumbers.add(34);

    this.test(classesUnderTest, lineNumbers);
  }

  @Test
  public void testProbesOfInnerClass() throws Exception {
    List<String> classesUnderTest = new ArrayList<String>();
    classesUnderTest.add(InnerClass.class.getCanonicalName());
    classesUnderTest.add(InnerClass.class.getCanonicalName() + "$InnerPrivateClass");
    classesUnderTest.add(InnerClass.class.getCanonicalName() + "$InnerPublicClass");

    List<Integer> lineNumbers = new ArrayList<Integer>();
    // $InnerClass
    lineNumbers.add(21);
    lineNumbers.add(22);
    lineNumbers.add(23);
    // $InnerClass$InnerPrivateClass
    lineNumbers.add(26);
    lineNumbers.add(27);
    lineNumbers.add(28);
    // $InnerClass$InnerPublicClass
    lineNumbers.add(33);
    lineNumbers.add(34);
    lineNumbers.add(35);
    lineNumbers.add(37);

    this.test(classesUnderTest, lineNumbers);
  }

  @Test
  public void testProbesOfPrivateClass() throws Exception {
    List<String> classesUnderTest = new ArrayList<String>();
    classesUnderTest.add(PrivateModifiers.class.getCanonicalName());

    List<Integer> lineNumbers = new ArrayList<Integer>();
    lineNumbers.add(20);
    lineNumbers.add(22);
    lineNumbers.add(25);
    lineNumbers.add(26);
    lineNumbers.add(28);

    this.test(classesUnderTest, lineNumbers);
  }

  @Test
  public void testProbesOfProtectedClass() throws Exception {
    List<String> classesUnderTest = new ArrayList<String>();
    classesUnderTest.add(ProtectedModifiers.class.getCanonicalName());

    List<Integer> lineNumbers = new ArrayList<Integer>();
    lineNumbers.add(19);
    lineNumbers.add(21);
    lineNumbers.add(24);
    lineNumbers.add(25);
    lineNumbers.add(27);

    this.test(classesUnderTest, lineNumbers);
  }

  @Test
  public void testProbesOfPublicClass() throws Exception {
    List<String> classesUnderTest = new ArrayList<String>();
    classesUnderTest.add(PublicModifiers.class.getCanonicalName());

    List<Integer> lineNumbers = new ArrayList<Integer>();
    lineNumbers.add(19);
    lineNumbers.add(21);
    lineNumbers.add(25);
    lineNumbers.add(26);
    lineNumbers.add(28);

    this.test(classesUnderTest, lineNumbers);
  }

  @Test
  public void testProbesOfPublicFinalClass() throws Exception {
    List<String> classesUnderTest = new ArrayList<String>();
    classesUnderTest.add(PublicFinalModifiers.class.getCanonicalName());

    List<Integer> lineNumbers = new ArrayList<Integer>();
    lineNumbers.add(19);
    lineNumbers.add(21);
    lineNumbers.add(24);
    lineNumbers.add(25);
    lineNumbers.add(27);

    this.test(classesUnderTest, lineNumbers);
  }

  @Test
  public void testProbesOfPublicStaticClass() throws Exception {
    List<String> classesUnderTest = new ArrayList<String>();
    classesUnderTest.add(PublicStaticModifiers.class.getCanonicalName());

    List<Integer> lineNumbers = new ArrayList<Integer>();
    lineNumbers.add(19);
    lineNumbers.add(21);
    lineNumbers.add(24);
    lineNumbers.add(25);
    lineNumbers.add(27);

    this.test(classesUnderTest, lineNumbers);
  }

  @Test
  public void testProbesOfDeprecatedClass() throws Exception {
    List<String> classesUnderTest = new ArrayList<String>();
    classesUnderTest.add(DeprecatedAnnotation.class.getCanonicalName());

    List<Integer> lineNumbers = new ArrayList<Integer>();
    lineNumbers.add(20);
    lineNumbers.add(22);
    lineNumbers.add(26);
    lineNumbers.add(27);
    lineNumbers.add(28);
    lineNumbers.add(30);
    lineNumbers.add(35);
    lineNumbers.add(36);
    lineNumbers.add(37);
    lineNumbers.add(39);

    this.test(classesUnderTest, lineNumbers);
  }

}
