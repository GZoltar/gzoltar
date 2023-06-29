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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ExampleInterfaceTest {

    private ExampleInterface exampleInterface;

    private PrintStream out = System.out;
    private ByteArrayOutputStream outputCaptor = new ByteArrayOutputStream();

    @Before
    public void init() {
        this.exampleInterface = new ExampleInterface();
        System.setOut(new PrintStream(outputCaptor));
    }

    @After
    public void tearDown() {
        System.setOut(out);
    }

    @Test
    public void test1() {
        this.exampleInterface.learn();
        assertEquals("Learn java", outputCaptor.toString().trim());
    }

    @Test
    public void test2() {
        assertEquals("Hello World", this.exampleInterface.supply());
    }

    @Test
    public void test3() {
        assertEquals(11, this.exampleInterface.getLength()); // should fail
    }
    
    @Test
    public void test4() {
        assertTrue(this.exampleInterface.checkLength());
    }

    @Test
    public void test5() {
        this.exampleInterface.referingPrint();
        assertEquals("Hello World", outputCaptor.toString().trim());
    }

    @Test
    public void test6() {
        assertEquals(-1, this.exampleInterface.referingCompareTo());
    }
}
