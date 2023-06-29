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

/**
 * Example class with a local variable syntax for lambda parameter. Feature introduced in Java 11
 */

public class LocalVariableSyntaxTest {

    private int a;
    private int b;
    private final int dummy = 0;

    LocalVariableSyntaxTest(int a, int b){
        this.a = a;
        this.b = b;
    }

    public int exec() {
        // Local variable syntax for lambda parameters
        MathOperation addition = (var a, var b) -> {if(a == dummy) return dummy; else return a + b;};
        int result = addition.calculate(this.a, this.b);
        return result;
    }

    // Functional interface for local variable syntax test
    interface MathOperation {
        int calculate(int a, int b);
    }
}
