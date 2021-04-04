package mypackage;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
    App faulty = new App();
    @Test
    public void test1() {
        assertEquals(3, faulty.mid(3,2, 1));
    }
    @Test
    public void test2(){
        assertEquals(2, faulty.mid(1,3,2));
    }
    @Test
    public void test3(){
        assertEquals(2, faulty.mid(2,1,3));
    }
    @Test
    public void test4(){
        assertEquals(2, faulty.mid(2,2,1));
    }
    @Test
    public void test5(){
        assertEquals(2,faulty.mid(2,1,2));
    }
    @Test
    public void test6(){
        assertEquals(2, faulty.mid(1,2,2));
    }

}
