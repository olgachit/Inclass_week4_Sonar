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
        logger.info("Select a language:");
        logger.info("1. English");
        logger.info("2. Finnish");
        logger.info("3. Japanese");
        logger.info("4. Swedish");
        int choice = scanner.nextInt();
        Locale locale;
        switch (choice) {
            case 1:
                locale = Locale.forLanguageTag("en-US");
                break;
            case 2:
                locale = Locale.forLanguageTag("fi-FI");
                break;
            case 3:
                locale = Locale.forLanguageTag("ja-JP");
                break;
            case 4:
                locale = Locale.forLanguageTag("sv-SE");
                break;
            default:
                locale = Locale.forLanguageTag("en-US");
        }
        ResourceBundle rb = ResourceBundle.getBundle("MessagesBundle", locale);

        logger.info(() -> rb.getString("prompt.items"));
        int itemCount = scanner.nextInt();

        double total = 0;

        for (int i = 1; i <= itemCount; i++) {
            String ordinal = getOrdinal(i);

            logger.info(() -> MessageFormat.format(rb.getString("prompt.price"), ordinal));
            double price = scanner.nextDouble();

            logger.info(() -> MessageFormat.format(rb.getString("prompt.quantity"), ordinal));
            int quantity = scanner.nextInt();

            double itemTotal = calculateItemTotal(price, quantity);
            logger.info(() -> rb.getString("total.items") + " " + itemTotal);
            total += itemTotal;
        }

        double finalTotal = total;
        logger.info(() -> rb.getString("total.cart") + " " + finalTotal);
    }
    public static double calculateItemTotal(double price, int quantity) {
        return price * quantity;
    }
}