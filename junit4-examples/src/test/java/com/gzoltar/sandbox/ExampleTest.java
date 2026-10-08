package com.gzoltar.sandbox;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class ExampleTest {
    @Test
    public void testPass() {
        assertTrue(true);
    }

    @Test
    public void testFail() {
        assertEquals(1, 2);
    }

    @Ignore("this test is skipped intentionally.")
    @Test
    public void testIgnore() {
        assertTrue(true);
    }
}