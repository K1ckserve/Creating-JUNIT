package studenttest;

import unittest.annotations.Test;

import static unittest.assertions.Assert.assertEquals;
import static unittest.assertions.Assert.assertTrue;

public class test1 {
    @Test
    public void testA(){
        assertTrue(false);
    }

    @Test
    public void testB(){
        int a =9;
        assertEquals(a,9);
    }

    @Test
    public void testC(){
        int a =9;
        assertEquals(a,8);
    }
}
