package org.gzoltar.java11;

/**
 * Exercises the reflection-visible side of Nest-Based Access Control (JEP 181, Java 11): a
 * top-level class and its nested classes are compiled into the same "nest", so they can access
 * each other's private members directly -- the compiler no longer needs to generate synthetic
 * bridge/accessor methods the way it did before Java 11. The new Class#getNestHost,
 * Class#getNestMembers, and Class#isNestmateOf reflection methods let you inspect this
 * relationship directly.
 */
public class NestFeatures {

  private int secret = 42;

  public class Inner {

    public int readOuterSecret() {
      // Direct access to the outer class's private field -- no synthetic accessor method is
      // generated for this, since NestFeatures and Inner belong to the same nest.
      return NestFeatures.this.secret;
    }

  }

  public Inner newInner() {
    return new Inner();
  }

  public boolean isNestmateOfInner() {
    return NestFeatures.class.isNestmateOf(Inner.class);
  }

  public Class<?> nestHost() {
    return Inner.class.getNestHost();
  }

  public int nestMemberCount() {
    return NestFeatures.class.getNestMembers().length;
  }

}
