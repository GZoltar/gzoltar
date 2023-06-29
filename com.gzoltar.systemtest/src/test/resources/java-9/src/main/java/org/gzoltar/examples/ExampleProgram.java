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

import java.util.List;
import java.util.stream.Collectors;

public class ExampleProgram implements ExampleInterface {

  @Override
  public String publicMethod() {
    return "public method";
  }

  public void printEvenNumBelowTen(List<Integer> numbers) {
    numbers.stream()
      .takeWhile(n -> n < 5) /* FAULT, n < 5 -> n < 10 */
      .filter(n -> n % 2 == 0)
      .forEach(System.out::println);
  }

  public void printOddNumbers(List<Integer> numbers) {
    List<Integer> oddNumbers = numbers.stream()
      .dropWhile(n -> n % 2 == 0)
      .collect(Collectors.toList());

    oddNumbers.forEach(System.out::println);
  }

  public String accessPrivateMethod() {
    return privateMethod();
  }
}
