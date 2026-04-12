package org.example;

import java.util.*;
import java.util.logging.Logger;
import java.text.MessageFormat;

public class ShoppingCart {
    private static final Logger logger = Logger.getLogger(ShoppingCart.class.getName());
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

        Locale locale = chooseLocale(scanner);
        ResourceBundle rb = ResourceBundle.getBundle("MessagesBundle", locale);

        double total = processCart(scanner, rb);

        logger.info(() -> rb.getString("total.cart") + " " + total);
    }

    public static double calculateItemTotal(double price, int quantity) {
        return price * quantity;
    }
    public static Locale chooseLocale(Scanner scanner) {
        logger.info("Select a language:");
        logger.info("1. English");
        logger.info("2. Finnish");
        logger.info("3. Japanese");
        logger.info("4. Swedish");

        switch (scanner.nextInt()) {
            case 2: return Locale.forLanguageTag("fi-FI");
            case 3: return Locale.forLanguageTag("ja-JP");
            case 4: return Locale.forLanguageTag("sv-SE");
            default: return Locale.forLanguageTag("en-US");
        }
    }

    public static double processCart(Scanner scanner, ResourceBundle rb) {
        logger.info(() -> rb.getString("prompt.items"));

        int itemCount = scanner.nextInt();
        double total = 0;

        for (int i = 1; i <= itemCount; i++) {
            String ordinal = getOrdinal(i);

            logger.info(() -> MessageFormat.format(rb.getString("prompt.price"), ordinal));
            double price = scanner.nextDouble();

            logger.info(() -> MessageFormat.format(rb.getString("prompt.quantity"), ordinal));
            int quantity = scanner.nextInt();

            total += calculateItemTotal(price, quantity);
        }

        return total;
    }
}