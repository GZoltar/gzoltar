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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ExampleProgramTest {

    private ExampleProgram exampleProgram;

    private PrintStream out = System.out;
    private ByteArrayOutputStream outputCaptor = new ByteArrayOutputStream();

    @Before
    public void init() {
        this.exampleProgram = new ExampleProgram();
        System.setOut(new PrintStream(outputCaptor));
    }

    @After
    public void tearDown() {
        System.setOut(out);
    }

    @Test
    public void test1() {
        var list = new ArrayList<Integer>();
        assertEquals(list, this.exampleProgram.varEmptyList());
    }

    @Test
    public void test2() {
        var list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);
        this.exampleProgram.printNumbers(list);
        assertEquals("1\n2\n3", outputCaptor.toString().trim());
    }

    @Test
    public void test3() {
        var list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);
        this.exampleProgram.printNumbers2(list);
        assertEquals("1\n2\n3", outputCaptor.toString().trim());
    }
    
}
