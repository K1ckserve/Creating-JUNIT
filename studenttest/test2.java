package studenttest;

import unittest.annotations.Test;

import static unittest.assertions.Assert.assertEquals;
import static unittest.assertions.Assert.assertTrue;

public class test2 {
    @Test
    public void testA(){
        assertTrue(true);
    }

    @Test
    public void testB(){
        int a =9;
        assertEquals(a,2+7);
    }

    @Test
    public void testC(){
        int a =9;
        assertEquals(a,8);
    }
}
