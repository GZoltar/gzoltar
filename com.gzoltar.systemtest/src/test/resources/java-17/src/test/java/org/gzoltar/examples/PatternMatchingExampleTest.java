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

import org.junit.Assert;
import org.junit.Test;

public class PatternMatchingExampleTest {

    @Test
    public void test1() {
        PatternMatchingExample example = new PatternMatchingExample();
        Object obj = "Hello";
        String result = example.process(obj);

        Assert.assertEquals("Received a String: olleH", result);
    }

    @Test
    public void test2() {
        PatternMatchingExample example = new PatternMatchingExample();
        Object obj = "Hello";
        String result = example.process(obj);

        Assert.assertEquals("Received a String: Hello", result);
    }

    //junit 4 test fails and junit5 passes
}
