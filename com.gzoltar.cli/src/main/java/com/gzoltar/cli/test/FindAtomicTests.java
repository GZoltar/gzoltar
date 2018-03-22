package com.gzoltar.cli.test;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import com.gzoltar.cli.test.junit.FindJUnitAtomicTests;
import com.gzoltar.cli.test.testng.FindTestNGAtomicTests;
import com.gzoltar.cli.utils.ClassType;

public abstract class FindAtomicTests {

  /**
   * 
   * @param testClasses
   * @return
   */
  public static List<String> find(final List<Pair<String, ClassType>> testClasses) {
    List<String> unitTests = new ArrayList<String>();

    for (Pair<String, ClassType> testClass : testClasses) {
      switch (testClass.getRight()) {
        case JUNIT:
          unitTests.addAll(FindJUnitAtomicTests.find(testClass.getLeft()));
          break;
        case TESTNG:
          unitTests.addAll(FindTestNGAtomicTests.find(testClass.getLeft()));
          break;
        default:
          continue;
      }
    }

    return unitTests;
  }
}
