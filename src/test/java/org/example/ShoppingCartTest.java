package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {
    @Test
    void testGetOrdinal() {
        assertEquals("1st", ShoppingCart.getOrdinal(1));
        assertEquals("2nd", ShoppingCart.getOrdinal(2));
        assertEquals("3rd", ShoppingCart.getOrdinal(3));
        assertEquals("4th", ShoppingCart.getOrdinal(4));
    }

    @Test
    void testCalculateItemTotal() {
        assertEquals(20.0, ShoppingCart.calculateItemTotal(10.0, 2));
        assertEquals(0.0, ShoppingCart.calculateItemTotal(10.0, 0));
        assertEquals(15.0, ShoppingCart.calculateItemTotal(5.0, 3));
    }
}