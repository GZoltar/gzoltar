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
public class PatternMatchingExample {
    public String process(Object obj) {
        if (obj instanceof Integer) {
            int value = (Integer) obj;
            return "Received an Integer: " + value;
        } else if (obj instanceof String str) {
            // Introduce a bug by mistakenly returning the reversed string
            return "Received a String: " + reverseString(str);
        } else {
            return "Received an unknown object";
        }
    }

    private String reverseString(String str) {
        // Bug: Reverse the string incorrectly
        return new StringBuilder(str).reverse().toString();
    }
}
