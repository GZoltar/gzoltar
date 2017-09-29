package org.gzoltar.examples;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AnonymousClass {

  public static void sort(List<Integer> list) {
    Collections.sort(list, new Comparator<Integer>() {
      @Override
      public int compare(Integer a, Integer b) {
        if (a > b) {
          return -1;
        } else if (a < b) {
          return 1;
        } else {
          return 0;
        }
      }
    });
  }

}
