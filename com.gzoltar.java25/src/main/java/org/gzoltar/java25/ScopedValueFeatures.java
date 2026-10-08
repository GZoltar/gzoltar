package org.gzoltar.java25;

/**
 * Scoped Values (JEP 506, finalized in Java 25).
 *
 * A ScopedValue shares immutable data across a call chain (and with child
 * threads spawned inside that chain) for the dynamic extent of a single
 * call, via ScopedValue.where(key, value).call(...)/.run(...). Unlike a
 * ThreadLocal, a scoped value is immutable, automatically un-bound once the
 * call finishes, and cannot be "leaked" or reassigned from inside the call.
 */
public class ScopedValueFeatures {

  private static final ScopedValue<String> CURRENT_USER = ScopedValue.newInstance();

  public String readInsideScope(String userName) throws Exception {
    return ScopedValue.where(CURRENT_USER, userName).call(CURRENT_USER::get);
  }

  public boolean isBoundOutsideScope() {
    return CURRENT_USER.isBound();
  }

  public String defaultWhenUnbound(String fallback) {
    return CURRENT_USER.orElse(fallback);
  }

  /** Demonstrates that an inner scope temporarily shadows the outer binding. */
  public String nestedScopedCall(String outer, String inner) throws Exception {
    return ScopedValue.where(CURRENT_USER, outer).call(() -> {
      String outerValue = CURRENT_USER.get();
      String innerValue = ScopedValue.where(CURRENT_USER, inner).call(CURRENT_USER::get);
      return outerValue + ":" + innerValue;
    });
  }

}
