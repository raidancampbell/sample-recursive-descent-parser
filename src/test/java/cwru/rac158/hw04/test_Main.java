package cwru.rac158.hw04;
/*
hopefully everything in here is self explanatory
 */

import static org.junit.Assert.*;
import org.junit.Test;


public class test_Main {

    @Test
    public void test_proper_grammar()
    {
        assertTrue(Main.testHarness("print x"));
        assertTrue(Main.testHarness("var x = 0"));
        assertTrue(Main.testHarness("x = y"));
        assertTrue(Main.testHarness("x"));
        assertTrue(Main.testHarness("1 + y"));
        assertTrue(Main.testHarness("y - 1"));
        assertTrue(Main.testHarness("1e3 - y + 1"));
        assertTrue(Main.testHarness("1 + y - 2.0"));
        assertTrue(Main.testHarness("f() + 1"));
        assertTrue(Main.testHarness("f( 1, 2, 3)"));
        assertTrue(Main.testHarness("f(1, 2) + 2.0 + x"));
        assertTrue(Main.testHarness("f(x) + g(1, h(1, 2), 7 - f(5.0))"));
    }

    @Test
    public void test_improper_grammar(){
        assertFalse(Main.testHarness("f(s, 3,)"));
        assertFalse(Main.testHarness("1 + -4"));
        assertFalse(Main.testHarness("+1"));
        assertFalse(Main.testHarness("x = var"));
        assertFalse(Main.testHarness("print print"));
        assertFalse(Main.testHarness("x = print y"));
        assertFalse(Main.testHarness("f(x, +3)"));
    }

    @Test
    public void test_grammar_abuse(){
        assertFalse(Main.testHarness("?"));
        assertFalse(Main.testHarness("!!!"));
        assertFalse(Main.testHarness(""));
    }
}

