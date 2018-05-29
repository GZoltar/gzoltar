package com.gzoltar.agent.rt;

import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Internal utility to load runtime configuration from system properties. System property keys are
 * prefixed with <code>gzoltar-agent.</code>. If the same property is defined twice the system
 * property takes precedence.
 */
public final class ConfigLoader {

  private static final String SYS_PREFIX = "gzoltar-agent.";

  private static final Pattern SUBST_PATTERN = Pattern.compile("\\$\\{([^\\}]+)\\}");

  private ConfigLoader() {
    // no instance
  }

  /**
   * 
   * @param system
   * @return
   */
  public static Properties load(final Properties system) {
    final Properties result = new Properties();
    loadSystemProperties(system, result);
    substSystemProperties(result, system);
    return result;
  }

  private static void loadSystemProperties(final Properties system, final Properties result) {
    for (final Map.Entry<Object, Object> entry : system.entrySet()) {
      final String keystr = entry.getKey().toString();
      if (keystr.startsWith(SYS_PREFIX)) {
        result.put(keystr.substring(SYS_PREFIX.length()), entry.getValue());
      }
    }
  }

  private static void substSystemProperties(final Properties result, final Properties system) {
    for (final Map.Entry<Object, Object> entry : result.entrySet()) {
      final String oldValue = (String) entry.getValue();
      final StringBuilder newValue = new StringBuilder();
      final Matcher m = SUBST_PATTERN.matcher(oldValue);
      int pos = 0;
      while (m.find()) {
        newValue.append(oldValue.substring(pos, m.start()));
        final String sub = system.getProperty(m.group(1));
        newValue.append(sub == null ? m.group(0) : sub);
        pos = m.end();
      }
      newValue.append(oldValue.substring(pos));
      entry.setValue(newValue.toString());
    }
  }

}
