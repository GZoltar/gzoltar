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
package org.gzoltar.examples;

import java.io.PrintStream;

import java.util.Comparator;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

// Functional Interface, i.e. Supplier, Consumer, Function, Predicate, etc.

public class ExampleInterface {

  // Consumer void accept(T t)

  private void Learn(String s, Consumer<String> consumer) {
    consumer.accept(s);
  }

  public void learn() {
    // Not using Lambda

    /*
    Learn("java", new Consumer<String>() {
      @Override
      public void accept(String s) {
        System.out.println("Learn " + s);
      }
    });
    */

    // Using Lambda
    Learn("java", (s) -> {
      System.out.println("Learn " + s);
    });
  }

  // Supplier T get()

  public String supply() {
    // Not using Lambda

    /* 
    Supplier<String> supplier = new Supplier<String>() {
      @Override
      public String get() {
        return "Hello World";
      }
    };
    */

    // Using Lambda
    Supplier<String> supplier = () -> {
      return "Hello World";
    };
    String s = supplier.get();
    return s;
  }

  // Function R apply(T t)

  public int getLength() {
    // Not using Lambda

    /*
    Function<String, Integer> function = new Function<String, Integer>() {
      @Override
      public Integer apply(String s) {
        return s.length();
      }
    };
    */

    // Using Lambda
    Function<String, Integer> function = (s) -> {
      return s.length()+1; /* FAULT, length()+1 -> length() */
    };
    int length = function.apply("Hello World");
    return length;
  }

  // Predicate boolean test(T t)

  public boolean checkLength() {
    // Not using Lambda

    /*
    Predicate<String> predicate = new Predicate<String>() {
      @Override
      public boolean test(String s) {
        return s.length() > 5;
      }
    };
    */

    // Using Lambda
    Predicate<String> predicate = (s) -> {
      return s.length() > 5;
    };
    boolean result = predicate.test("Hello World");
    return result;
  }

  // Reference to methods
  public void referingPrint() {
    // Using Lambda
    //Consumer<String> con1 = str -> System.out.println(str);

    // Using Method Reference
    PrintStream out = System.out;
    Consumer<String> con2 = out::println;
    con2.accept("Hello World");
  }

  public int referingCompareTo() {
    // Using Lambda
    /*
    Comparator<Integer> com1 = (x, y) -> Integer.compare(x, y);
    Integer result = com1.compare(1, 2);
    */

    // Using Method Reference
    Comparator<Integer> com2 = Integer::compareTo;
    int result = com2.compare(1, 2);
    return result;
  }
}
