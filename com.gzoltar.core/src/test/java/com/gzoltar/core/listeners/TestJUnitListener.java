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
package com.gzoltar.core.listeners;

import java.util.ArrayList;
import java.util.List;
import org.gzoltar.examples.EnumClass;
import org.gzoltar.examples.tests.TestEnumClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.events.EmptyEventListener;
import com.gzoltar.core.instr.InstrumentationLevel;
import com.gzoltar.core.instr.Instrumenter;
import com.gzoltar.core.instr.granularity.GranularityLevel;
import com.gzoltar.core.runtime.Collector;
import com.gzoltar.core.spectrum.ISpectrum;
import javassist.ClassPool;

public class TestJUnitListener {

  private final static ClassPool pool = ClassPool.getDefault();

  @Test
  public void test() throws Exception {
    List<String> classesUnderTest = new ArrayList<String>();
    classesUnderTest.add(EnumClass.class.getCanonicalName());

    List<String> lineNumbers = new ArrayList<String>();
    lineNumbers.add("3");
    lineNumbers.add("8");
    lineNumbers.add("13");
    lineNumbers.add("18");

    // --------------

    AgentConfigs configs = new AgentConfigs();
    configs.setGranularity(GranularityLevel.LINE);
    configs.setInstrumentationLevel(InstrumentationLevel.NONE);

    Collector.instance().addListener(new EmptyEventListener());

    Instrumenter instrumenter = new Instrumenter(configs);
    for (String classUnderTest : classesUnderTest) {
      instrumenter.instrument(pool.get(classUnderTest));
    }
    JUnitCore core = new JUnitCore();
    core.addListener(new JUnitListener());
    core.run(TestEnumClass.class);

    ISpectrum spectrum = Collector.instance().getSpectrum();
    spectrum.toString();
  }

}
