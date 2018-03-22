package com.gzoltar.cli.utils;

public final class SystemProperties {

  public static final String PATH_SEPARATOR = System.getProperty("path.separator");
  public static final String FILE_SEPARATOR = System.getProperty("file.separator");
  public static final String JAVA_HOME = System.getProperty("java.home")
      + SystemProperties.FILE_SEPARATOR + "bin" + SystemProperties.FILE_SEPARATOR + "java";

  public static final String OS_NAME = System.getProperty("os.name").toLowerCase();

  /**
   * 
   * @return
   */
  public static String getClasspathString() {
    return System.getProperty("java.class.path");
  }

  /**
   * 
   * @return
   */
  public static String[] getClasspathArray() {
    return System.getProperty("java.class.path").split(SystemProperties.PATH_SEPARATOR);
  }
}
