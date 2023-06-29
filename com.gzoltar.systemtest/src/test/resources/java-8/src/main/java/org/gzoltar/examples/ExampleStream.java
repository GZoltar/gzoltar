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

import java.util.Arrays;
import java.util.List;

import java.util.stream.Collectors;

public class ExampleStream {

    public List<Integer> findOddNumbers(List<Integer> numbers) {
        return numbers.stream()
                .filter(n -> n % 2 == 0) /* FAULT, n % 2 == 0 -> n % 2 != 0 */
                .collect(Collectors.toList());
    }

    public List<Integer> filterRepeatedNumbers(List<Integer> numbers) {
        return numbers.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    public void printNumbers(List<Integer> numbers) {
        numbers.stream()
                .forEach(System.out::println);
    }
    
    public void printNumbers2(List<Integer> numbers) {
        numbers.stream()
                .forEach(
                    n -> System.out.println(n + 1) /* FAULT, n + 1 -> n */
                ); 
    }
}
