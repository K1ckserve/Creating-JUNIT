package studenttest;

import unittest.annotations.Order;
import unittest.annotations.Ordered;
import unittest.annotations.Test;

import static unittest.assertions.Assert.assertEquals;
import static unittest.assertions.Assert.assertTrue;
@Ordered

public class test3 {
    @Test
    @Order(3)
    public void testA(){
        assertTrue(true);
    }

    @Test
    @Order(1)
    public void testB(){
        int a =9;
        assertEquals(a,2+7);
    }

    @Test
    @Order(2)
    public void testC(){
        int a =9;
        assertEquals(a,8);
    }
}
