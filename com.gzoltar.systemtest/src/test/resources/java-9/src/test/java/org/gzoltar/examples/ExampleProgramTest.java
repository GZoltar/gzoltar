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

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
    assertEquals("public method", this.exampleProgram.publicMethod());
  }

  @Test
  public void test2() {
    assertEquals("private method", this.exampleProgram.accessPrivateMethod());
  }

  @Test
  public void test3() {
    this.exampleProgram.printEvenNumBelowTen(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
    assertEquals("2\n4\n6\n8", outputCaptor.toString().trim());
  }

  @Test
  public void test4() {
    this.exampleProgram.printOddNumbers(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
    assertEquals("1\n3\n5\n7\n9", outputCaptor.toString().trim());
  }

  @Ignore("Not yet fully implemented")
  public void test5() {
    // TBD
  }
}
