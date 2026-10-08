package com.gzoltar.sandbox;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExampleTest {
    @Test
    void testPass() {
        assertTrue(true);
    }

    @Test
    void testFail() {
        assertEquals(1, 2);
    }

    @Disabled("this test is skipped intentionally.")
    @Test
    void testIgnore() {
        assertTrue(true);
    }
}