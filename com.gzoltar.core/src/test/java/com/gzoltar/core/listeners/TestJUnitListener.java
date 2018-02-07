package com.gzoltar.core.listeners;

import java.util.ArrayList;
import java.util.List;
import org.gzoltar.examples.EnumClass;
import org.gzoltar.examples.tests.TestEnumClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.instr.Instrumenter;
import com.gzoltar.core.instr.granularity.GranularityLevel;
import com.gzoltar.core.instr.pass.InstrumentationPass;
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

    AgentConfigs configs = new AgentConfigs(null);
    configs.setGranularity(GranularityLevel.LINE);

    Collector.start(configs.getEventListener());

    Instrumenter instrumenter = new Instrumenter(configs);
    for (String classUnderTest : classesUnderTest) {
      instrumenter.instrument(pool.get(classUnderTest));
    }
    JUnitCore core = new JUnitCore();
    core.addListener(new JUnitListener());
    core.run(TestEnumClass.class);

    ISpectrum spectrum = Collector.instance().getSpectrumListener().getSpectrum();
    spectrum.toString();
  }

}
