package com.gzoltar.cli.utils;

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
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import com.gzoltar.core.instr.Outcome;
import com.gzoltar.core.instr.actions.BlackList;
import com.gzoltar.core.instr.actions.WhiteList;
import com.gzoltar.core.instr.filter.Filter;
import com.gzoltar.core.instr.matchers.ClassNameMatcher;
import com.gzoltar.core.instr.matchers.JUnitMatcher;
import com.gzoltar.core.instr.matchers.PrefixMatcher;
import com.gzoltar.core.instr.matchers.TestNGMatcher;
import javassist.ClassPool;
import javassist.CtClass;

public class ClasspathHandler {

  private static ClassPool classPool = ClassPool.getDefault();

  private Filter filter;

  public ClasspathHandler(final String includes, final String excludes) {

    // exclude *all* GZoltar's runtime classes from loading
    BlackList excludeGZoltarClasses = new BlackList(new PrefixMatcher("com.gzoltar."));

    // load some classes
    WhiteList includeClasses = new WhiteList(new ClassNameMatcher(includes));
    // do not load some classes
    BlackList excludeClasses = new BlackList(new ClassNameMatcher(excludes));

    this.filter = new Filter(excludeGZoltarClasses, includeClasses, excludeClasses);
  }

  /**
   * Returns all JUnit/TestNG classes in the classpath
   * 
   * @return
   */
  public List<Pair<String, ClassType>> getTestClasses() {
    this.filter.add(new WhiteList(new JUnitMatcher()));
    this.filter.add(new WhiteList(new TestNGMatcher()));
    return this.getClasses();
  }

  /**
   * Returns all classes that are not JUnit/TestNG classes in the classpath
   * 
   * @return
   */
  public List<Pair<String, ClassType>> getNonTestClasses() {
    this.filter.add(new BlackList(new JUnitMatcher()));
    this.filter.add(new BlackList(new TestNGMatcher()));
    return this.getClasses();
  }

  private List<Pair<String, ClassType>> getClasses() {
    List<Pair<String, ClassType>> classes = new ArrayList<Pair<String, ClassType>>();

    for (String cp : SystemProperties.getClasspathArray()) {
      this.processClassPath(cp, classes);
    }

    return classes;
  }

  private void processClassPath(final String cp, final List<Pair<String, ClassType>> classes) {

    try {
      File f = new File(cp);
      if (!f.exists()) {
        System.err.println("File/Directory '" + cp + "' does not exist!");
        return;
      }
      if (!f.canRead()) {
        System.err.println("No permission to read file/directory '" + cp + "'!");
        return;
      }

      if (f.isDirectory()) {
        // get all .class, and .jar files
        for (File file : FileUtils.listFiles(f, new String[] {"class", "jar"}, true)) {
          this.processClassPath(file.getAbsolutePath(), classes);
        }
      } else if (f.getAbsolutePath().endsWith(".jar")) {
        // iterate over all (.class, or .jar) files in the .jar
        JarFile jarFile = new JarFile(f.getAbsolutePath());

        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
          JarEntry entry = entries.nextElement();
          String entryName = entry.getName();

          if (entryName.endsWith(".class")) {
            InputStream in = jarFile.getInputStream(entry);

            CtClass cc = classPool.makeClass(in);
            if (this.isAllowed(cc)) {
              ClassType type = this.getClassType(cc);
              classes.add(new ImmutablePair<String, ClassType>(cc.getName(), type));
            }

            in.close();
          } else if (entryName.endsWith(".jar")) {
            // TODO
            // create a temporary .jar file with the content of 'entryName'
          }
        }

        jarFile.close();
      } else if (f.getAbsolutePath().endsWith(".class")) {
        FileInputStream fin = new FileInputStream(f);
        CtClass cc = classPool.makeClass(fin);
        if (this.isAllowed(cc)) {
          ClassType type = this.getClassType(cc);
          classes.add(new ImmutablePair<String, ClassType>(cc.getName(), type));
        }

        fin.close();
      }
    } catch (IOException e) {
      System.err.println(e);
    }
  }

  private boolean isAllowed(final CtClass cc) {
    return this.filter.filter(cc) == Outcome.REJECT ? false : true;
  }

  private ClassType getClassType(final CtClass cc) {

    if (new JUnitMatcher().matches(cc)) {
      return ClassType.JUNIT;
    }

    if (new TestNGMatcher().matches(cc)) {
      return ClassType.TESTNG;
    }

    return ClassType.CLASS;
  }
}
