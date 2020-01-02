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
package com.gzoltar.core.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.apache.commons.io.FileUtils;
import org.jacoco.core.runtime.WildcardMatcher;
import com.gzoltar.core.util.ClassType;
import com.gzoltar.core.instr.Outcome;
import com.gzoltar.core.instr.actions.WhiteList;
import com.gzoltar.core.instr.filter.Filter;
import com.gzoltar.core.instr.matchers.JUnitMatcher;
import com.gzoltar.core.instr.matchers.OrMatcher;
import com.gzoltar.core.instr.matchers.TestNGMatcher;
import com.gzoltar.core.test.junit.FindJUnitTestMethods;
import com.gzoltar.core.test.testng.FindTestNGTestMethods;
import javassist.ClassPool;
import javassist.CtClass;

public abstract class FindTestMethods {

  private static final Filter testClassesFilter =
      new Filter(new WhiteList(new OrMatcher(new JUnitMatcher(), new TestNGMatcher())));

  private static final ClassPool classPool = ClassPool.getDefault();

  /**
   * 
   * @param path
   * @param testsMatcher
   * @return
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public static List<TestMethod> findTestMethodsInPath(final File path,
      final WildcardMatcher testsMatcher) throws IOException, ClassNotFoundException {
    final List<TestMethod> testMethods = new ArrayList<TestMethod>();

    if (!path.exists()) {
      throw new RuntimeException("File/Directory '" + path.getAbsolutePath() + "' does not exist!");
    }
    if (!path.canRead()) {
      throw new RuntimeException(
          "No permission to read file/directory '" + path.getAbsolutePath() + "'!");
    }

    if (path.isDirectory()) {
      // get all .class, and .jar files
      for (File file : FileUtils.listFiles(path, new String[] {"class", "jar"}, true)) {
        testMethods.addAll(findTestMethodsInPath(file, testsMatcher));
      }
    } else if (path.getAbsolutePath().endsWith(".jar")) {
      // iterate over all (.class, or .jar) files in the .jar
      JarFile jarFile = new JarFile(path.getAbsolutePath());

      Enumeration<JarEntry> entries = jarFile.entries();
      while (entries.hasMoreElements()) {
        JarEntry entry = entries.nextElement();
        String entryName = entry.getName();

        if (entryName.endsWith(".class")) {
          InputStream in = jarFile.getInputStream(entry);

          CtClass ctClass = classPool.makeClassIfNew(in);
          testMethods.addAll(findTestMethodsInClass(testsMatcher, ctClass));
          ctClass.detach();

          in.close();
        } else if (entryName.endsWith(".jar")) {
          // TODO
          // create a temporary .jar file with the content of 'entryName'
        }
      }

      jarFile.close();
    } else if (path.getAbsolutePath().endsWith(".class")) {
      FileInputStream fin = new FileInputStream(path);

      CtClass ctClass = classPool.makeClassIfNew(fin);
      testMethods.addAll(findTestMethodsInClass(testsMatcher, ctClass));
      ctClass.detach();

      fin.close();
    }

    return testMethods;
  }

  /**
   * 
   * @param testsMatcher
   * @param ctClass
   * @return
   * @throws ClassNotFoundException
   */
  private static List<TestMethod> findTestMethodsInClass(final WildcardMatcher testsMatcher,
      final CtClass ctClass) throws ClassNotFoundException {
    if (testClassesFilter.filter(ctClass) == Outcome.REJECT) {
      return new ArrayList<TestMethod>(0);
    }

    ClassType classType = getClassType(ctClass);
    switch (classType) {
      case JUNIT:
        return FindJUnitTestMethods.find(testsMatcher, ctClass.getName());
      case TESTNG:
        return FindTestNGTestMethods.find(testsMatcher, ctClass.getName());
      default:
        return new ArrayList<TestMethod>(0);
    }
  }

  private static ClassType getClassType(final CtClass ctClass) {
    if (new JUnitMatcher().matches(ctClass)) {
      return ClassType.JUNIT;
    }
    if (new TestNGMatcher().matches(ctClass)) {
      return ClassType.TESTNG;
    }
    return ClassType.CLASS;
  }
}
