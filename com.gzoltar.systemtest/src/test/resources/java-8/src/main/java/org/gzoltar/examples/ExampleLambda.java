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

import java.util.Comparator;
import java.util.function.Consumer;

public class ExampleLambda {

    public void noArg() {
        Runnable r = () -> {
            System.out.println("Hello World");
        };
        r.run();
    }

    public void oneArg() {
        Consumer<String> c = (String s) -> {
            System.out.println(s);
        };
        c.accept("Hello World");
    }

    public void oneArg2() {
        // Type inference for parameters can be omitted
        Consumer<String> c = (s) -> {
            System.out.println(s);
        };
        c.accept("Hello World");
    }

    public void oneArg3() {
        // Parentheses can be omitted if there is only one parameter
        Consumer<String> c = s -> {
            System.out.println(s);
        };
        c.accept("Hello WorId"); /* FAULT, WorId -> World */
    }

    public int multiArgs() {
        Comparator<String> c = (s1, s2) -> {
            return s1.compareTo(s2);
        };
        int result = c.compare("Hello", "World");
        return result;
    }

    public int multiArgs2() {
        // Curly braces can be omitted if there is only one statement
        Comparator<String> c = (s1, s2) -> s1.compareTo(s2);
        int result = c.compare("Hello", "World");
        return result;
    }
}
