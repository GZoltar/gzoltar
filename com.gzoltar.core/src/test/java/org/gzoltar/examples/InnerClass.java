package org.gzoltar.examples;

public class InnerClass {

  public InnerClass() {
    new InnerPrivateClass();
  }

  private class InnerPrivateClass {
    public InnerPrivateClass() {
      System.out.println("InnerPrivateClass constructor");
    }
  }

  public class InnerPublicClass {

    public InnerPublicClass(int x) {
      for (int i = 0; i < x; i++) {
        System.out.println("InnerPublicClass: " + i);
      }
    }
  }

}
