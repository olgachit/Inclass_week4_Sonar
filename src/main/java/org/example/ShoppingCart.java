package org.example;

import java.util.*;

public class ShoppingCart {
    public static String getOrdinal(int number) {
        if (number >= 11 && number <= 13) {
            return number + "th";
        }
        switch (number % 10) {
            case 1: return number + "st";
            case 2: return number + "nd";
            case 3: return number + "rd";
            default: return number + "th";
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select a language:");
        System.out.println("1. English");
        System.out.println("2. Finnish");
        System.out.println("3. Japanese");
        System.out.println("4. Swedish");
        int choice = scanner.nextInt();
        Locale locale;
        switch (choice) {
            case 1:
                locale = new Locale("en", "US");
                break;
            case 2:
                locale = new Locale("fi", "FI");
                break;
            case 3:
                locale = new Locale("ja", "JP");
                break;
            case 4:
                locale = new Locale("sv", "SE");
                break;
            default:
                locale = new Locale("en", "US");
        }
        ResourceBundle rb = ResourceBundle.getBundle("MessagesBundle", locale);

        System.out.println(rb.getString("prompt.items"));
        int itemCount = scanner.nextInt();

        double total = 0;

        for (int i = 1; i <= itemCount; i++) {
            String ordinal = getOrdinal(i);

            System.out.println(rb.getString("prompt.price").replace("{0}", ordinal));
            double price = scanner.nextDouble();

            System.out.println(rb.getString("prompt.quantity").replace("{0}", ordinal));
            int quantity = scanner.nextInt();

            double itemTotal = calculateItemTotal(price, quantity);
            System.out.println(rb.getString("total.items")+" "+itemTotal);
            total += itemTotal;
        }

        System.out.println(rb.getString("total.cart") + " " + total);
    }
    public static double calculateItemTotal(double price, int quantity) {
        return price * quantity;
    }
}