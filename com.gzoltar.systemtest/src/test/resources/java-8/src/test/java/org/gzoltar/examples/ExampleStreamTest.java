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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ExampleStreamTest {
    
    private ExampleStream exampleStream;

    private PrintStream out = System.out;
    private ByteArrayOutputStream outputCaptor = new ByteArrayOutputStream();

    @Before
    public void init() {
        this.exampleStream = new ExampleStream();
        System.setOut(new PrintStream(outputCaptor));
    }

    @After
    public void tearDown() {
        System.setOut(out);
    }

    @Test
    public void test1() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> expected = Arrays.asList(1, 3, 5);
        assertEquals(expected, this.exampleStream.findOddNumbers(numbers));
    }

    @Test
    public void test2() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 5, 4, 3, 2, 1);
        List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5);
        assertEquals(expected, this.exampleStream.filterRepeatedNumbers(numbers));
    }

    @Test
    public void test3() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        this.exampleStream.printNumbers(numbers);
        assertEquals("1\n2\n3\n4\n5", outputCaptor.toString().trim());
    }

    @Test
    public void test4() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        this.exampleStream.printNumbers2(numbers);
        assertEquals("1\n2\n3\n4\n5", outputCaptor.toString().trim());
    }
}
