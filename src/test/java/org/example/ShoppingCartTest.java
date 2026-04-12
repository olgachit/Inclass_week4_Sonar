package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

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
    void testGetOrdinal_specialCases() {
        assertEquals("11th", ShoppingCart.getOrdinal(11));
        assertEquals("12th", ShoppingCart.getOrdinal(12));
        assertEquals("13th", ShoppingCart.getOrdinal(13));
    }

    @Test
    void testGetOrdinal_higherNumbers() {
        assertEquals("21st", ShoppingCart.getOrdinal(21));
        assertEquals("22nd", ShoppingCart.getOrdinal(22));
        assertEquals("23rd", ShoppingCart.getOrdinal(23));
        assertEquals("24th", ShoppingCart.getOrdinal(24));
    }

    @Test
    void testCalculateItemTotal() {
        assertEquals(20.0, ShoppingCart.calculateItemTotal(10.0, 2));
        assertEquals(0.0, ShoppingCart.calculateItemTotal(10.0, 0));
        assertEquals(15.0, ShoppingCart.calculateItemTotal(5.0, 3));
    }

    @Test
    void testCalculateItemTotal_edgeCases() {
        assertEquals(0.0, ShoppingCart.calculateItemTotal(0.0, 5));
        assertEquals(7.5, ShoppingCart.calculateItemTotal(2.5, 3));
    }

    @Test
    void testProcessCart() {
        String input = "2\n10\n2\n5\n3\n"; // itemCount=2

        Scanner scanner = new Scanner(input);
        ResourceBundle rb = ResourceBundle.getBundle("i18n.MessagesBundle", Locale.forLanguageTag("fi-FI"));
        double result = ShoppingCart.processCart(scanner, rb);

        assertEquals(35.0, result);
    }

    @ParameterizedTest
    @CsvSource({
            "1, en-US",
            "2, fi-FI",
            "3, ja-JP",
            "4, sv-SE",
            "999, en-US"   // default case test
    })
    void testChooseLocale(int input, String expectedLocale) {

        Scanner scanner = new Scanner(String.valueOf(input));

        Locale result = ShoppingCart.chooseLocale(scanner);

        assertEquals(Locale.forLanguageTag(expectedLocale), result);
    }
}