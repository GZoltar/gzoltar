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
package com.gzoltar.cli.test;

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
import com.gzoltar.cli.test.junit.FindJUnitTestMethods;
import com.gzoltar.cli.test.testng.FindTestNGTestMethods;
import com.gzoltar.cli.utils.ClassType;
import com.gzoltar.core.instr.Outcome;
import com.gzoltar.core.instr.filter.Filter;
import com.gzoltar.core.instr.matchers.JUnitMatcher;
import com.gzoltar.core.instr.matchers.TestNGMatcher;
import javassist.ClassPool;
import javassist.CtClass;

public abstract class FindTestMethods {

  private static final ClassPool classPool = ClassPool.getDefault();

  /**
   * 
   * @param testClasses
   * @return
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public static List<TestMethod> findTestMethodsInPath(final Filter filter, final File path)
      throws IOException, ClassNotFoundException {
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
        testMethods.addAll(findTestMethodsInPath(filter, file));
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
          testMethods.addAll(findTestMethodsInClass(filter, ctClass));
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
      testMethods.addAll(findTestMethodsInClass(filter, ctClass));
      ctClass.detach();

      fin.close();
    }

    return testMethods;
  }

  /**
   * 
   * @param filter
   * @param ctClass
   * @return
   * @throws ClassNotFoundException
   */
  public static List<TestMethod> findTestMethodsInClass(final Filter filter, final CtClass ctClass)
      throws ClassNotFoundException {
    if (!isAllowed(filter, ctClass)) {
      return new ArrayList<TestMethod>();
    }

    ClassType classType = getClassType(ctClass);
    switch (classType) {
      case JUNIT:
        return FindJUnitTestMethods.find(ctClass.getName());
      case TESTNG:
        return FindTestNGTestMethods.find(ctClass.getName());
      default:
        return new ArrayList<TestMethod>();
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

  private static boolean isAllowed(final Filter filter, final CtClass ctClass) {
    return filter.filter(ctClass) == Outcome.REJECT ? false : true;
  }
}
