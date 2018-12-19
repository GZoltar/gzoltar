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
package com.gzoltar.core.instr.pass;

import static org.junit.Assert.assertEquals;
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

  private void test(List<String> classesUnderTest, List<String> lineNumbers) throws Exception {
    AgentConfigs configs = new AgentConfigs();
    configs.setGranularity(GranularityLevel.LINE);

    Collector.instance().addListener(configs.getEventListener());

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
      assertEquals(lineNumbers.get(i), leafs.get(i).getName());
    }

  }

  @Test
  public void testProbesOfEnumClass() throws Exception {
    List<String> classesUnderTest = new ArrayList<String>();
    classesUnderTest.add(EnumClass.class.getCanonicalName());

    List<String> lineNumbers = new ArrayList<String>();
    lineNumbers.add("3");
    lineNumbers.add("8");
    lineNumbers.add("13");
    lineNumbers.add("18");

    this.test(classesUnderTest, lineNumbers);
  }

  @Test
  public void testProbesOfInterfaceClass() throws Exception {
    List<String> classesUnderTest = new ArrayList<String>();
    classesUnderTest.add(InterfaceClass.class.getCanonicalName());

    this.test(classesUnderTest, new ArrayList<String>());
  }

  @Test
  public void testProbesOfAbstractClass() throws Exception {
    List<String> classesUnderTest = new ArrayList<String>();
    classesUnderTest.add(AbstractClass.class.getCanonicalName());

    List<String> lineNumbers = new ArrayList<String>();
    lineNumbers.add("3");
    lineNumbers.add("8");

    this.test(classesUnderTest, lineNumbers);
  }

  @Test
  public void testProbesOfAnonymousClass() throws Exception {
    List<String> classesUnderTest = new ArrayList<String>();
    classesUnderTest.add(AnonymousClass.class.getCanonicalName());
    classesUnderTest.add(AnonymousClass.class.getCanonicalName() + "$1");

    List<String> lineNumbers = new ArrayList<String>();
    lineNumbers.add("7");
    lineNumbers.add("10");
    lineNumbers.add("22");
    lineNumbers.add("10");
    lineNumbers.add("13");
    lineNumbers.add("14");
    lineNumbers.add("15");
    lineNumbers.add("16");
    lineNumbers.add("18");

    this.test(classesUnderTest, lineNumbers);
  }

  @Test
  public void testProbesOfInnerClass() throws Exception {
    List<String> classesUnderTest = new ArrayList<String>();
    classesUnderTest.add(InnerClass.class.getCanonicalName());
    classesUnderTest.add(InnerClass.class.getCanonicalName() + "$InnerPrivateClass");
    classesUnderTest.add(InnerClass.class.getCanonicalName() + "$InnerPublicClass");

    List<String> lineNumbers = new ArrayList<String>();
    lineNumbers.add("5");
    lineNumbers.add("6");
    lineNumbers.add("7");
    lineNumbers.add("10");
    lineNumbers.add("11");
    lineNumbers.add("12");
    lineNumbers.add("17");
    lineNumbers.add("18");
    lineNumbers.add("19");
    lineNumbers.add("21");

    this.test(classesUnderTest, lineNumbers);
  }

  @Test
  public void testProbesOfPrivateClass() throws Exception {
    List<String> classesUnderTest = new ArrayList<String>();
    classesUnderTest.add(PrivateModifiers.class.getCanonicalName());

    List<String> lineNumbers = new ArrayList<String>();
    lineNumbers.add("4");
    lineNumbers.add("6");
    lineNumbers.add("9");
    lineNumbers.add("10");
    lineNumbers.add("12");

    this.test(classesUnderTest, lineNumbers);
  }

  @Test
  public void testProbesOfProtectedClass() throws Exception {
    List<String> classesUnderTest = new ArrayList<String>();
    classesUnderTest.add(ProtectedModifiers.class.getCanonicalName());

    List<String> lineNumbers = new ArrayList<String>();
    lineNumbers.add("3");
    lineNumbers.add("5");
    lineNumbers.add("8");
    lineNumbers.add("9");
    lineNumbers.add("11");

    this.test(classesUnderTest, lineNumbers);
  }

  @Test
  public void testProbesOfPublicClass() throws Exception {
    List<String> classesUnderTest = new ArrayList<String>();
    classesUnderTest.add(PublicModifiers.class.getCanonicalName());

    List<String> lineNumbers = new ArrayList<String>();
    lineNumbers.add("3");
    lineNumbers.add("5");
    lineNumbers.add("9");
    lineNumbers.add("10");
    lineNumbers.add("12");

    this.test(classesUnderTest, lineNumbers);
  }

  @Test
  public void testProbesOfPublicFinalClass() throws Exception {
    List<String> classesUnderTest = new ArrayList<String>();
    classesUnderTest.add(PublicFinalModifiers.class.getCanonicalName());

    List<String> lineNumbers = new ArrayList<String>();
    lineNumbers.add("3");
    lineNumbers.add("5");
    lineNumbers.add("8");
    lineNumbers.add("9");
    lineNumbers.add("11");

    this.test(classesUnderTest, lineNumbers);
  }

  @Test
  public void testProbesOfPublicStaticClass() throws Exception {
    List<String> classesUnderTest = new ArrayList<String>();
    classesUnderTest.add(PublicStaticModifiers.class.getCanonicalName());

    List<String> lineNumbers = new ArrayList<String>();
    lineNumbers.add("5");
    lineNumbers.add("3");
    lineNumbers.add("8");
    lineNumbers.add("9");
    lineNumbers.add("11");

    this.test(classesUnderTest, lineNumbers);
  }

  @Test
  public void testProbesOfDeprecatedClass() throws Exception {
    List<String> classesUnderTest = new ArrayList<String>();
    classesUnderTest.add(DeprecatedAnnotation.class.getCanonicalName());

    List<String> lineNumbers = new ArrayList<String>();
    lineNumbers.add("4");
    lineNumbers.add("6");
    lineNumbers.add("10");
    lineNumbers.add("11");
    lineNumbers.add("12");
    lineNumbers.add("14");
    lineNumbers.add("19");
    lineNumbers.add("20");
    lineNumbers.add("21");
    lineNumbers.add("23");

    this.test(classesUnderTest, lineNumbers);
  }

}
