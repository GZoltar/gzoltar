package org.gzoltar.examples;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.Assert;

public class LocalVariableTest {

    private LocalVariableSyntaxTest lvsTest;

    @Test
    public void test1() {
        this.lvsTest = new LocalVariableSyntaxTest(1,2);
        assertEquals(3,this.lvsTest.exec());
    }

    @Test
    public void test2() {
        this.lvsTest = new LocalVariableSyntaxTest(9,3);
        assertEquals(12,this.lvsTest.exec());
    }

    @Test
    public void test3() {
        this.lvsTest = new LocalVariableSyntaxTest(0,2); // IT FAILS
        assertEquals(2,this.lvsTest.exec());
    }

    @Ignore("Not yet fully implemented")
    public void test9() {
        // TBD
    }

}
