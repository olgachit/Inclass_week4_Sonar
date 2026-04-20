package org.example;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.example.service.CartService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import javafx.embed.swing.JFXPanel;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ControllerTest {

    private Controller controller;
    private CartService cartService;

    @BeforeAll
    static void initJfx() {
        new JFXPanel();
    }

    @BeforeEach
    void setUp() {
        cartService = mock(CartService.class);
        controller = new Controller(cartService, new HashMap<>());

        controller.languageComboBox = new ComboBox<>();
        controller.itemCountField = new TextField();
        controller.priceField = new TextField();
        controller.quantityField = new TextField();
        controller.itemLabel = new Label();
        controller.totalLabel = new Label();
        controller.root = new VBox();
        controller.appName = new Label();
        controller.startButton = new Button();
        controller.addItemButton = new Button();

        Map<String, String> rb = new HashMap<>();
        rb.put("total.cart", "Total:");
        rb.put("label.item", "Item:");
        rb.put("total.items", "Item total:");
        rb.put("error.invalid", "Invalid");
        rb.put("app.name", "App");
        rb.put("button.start", "Start");
        rb.put("button.add", "Add");
        rb.put("prompt.items", "Items");
        rb.put("prompt.price", "Price");
        rb.put("prompt.quantity", "Quantity");

        controller.setResourceBundle(rb);
    }

    @Test
    void handleStart() {
        controller.itemCountField.setText("2");

        when(cartService.saveCartRecord(anyInt(), anyDouble(), anyString()))
                .thenReturn(1);

        controller.handleStart();

        assertEquals("Item: 1", controller.itemLabel.getText());
        assertEquals("Total: 0.0", controller.totalLabel.getText());

        verify(cartService).saveCartRecord(2, 0.0, "en");

        assertFalse(controller.priceField.isDisabled());
        assertFalse(controller.quantityField.isDisabled());
        assertFalse(controller.addItemButton.isDisabled());
    }

    @Test
    void handleAddItemUpdatesTotalAndMovesNext() {
        controller.itemCountField.setText("2");

        when(cartService.saveCartRecord(anyInt(), anyDouble(), anyString()))
                .thenReturn(1);

        controller.handleStart();

        controller.priceField.setText("10");
        controller.quantityField.setText("2");

        controller.handleAddItem();

        assertEquals("Item total: 20.0", controller.totalLabel.getText());
        assertEquals("Item: 2", controller.itemLabel.getText());

        verify(cartService).saveCartItem(1, 1, 10.0, 2);
    }

    @Test
    void handleAddItemFinalItemDisablesInputs() {
        controller.itemCountField.setText("1");

        when(cartService.saveCartRecord(anyInt(), anyDouble(), anyString()))
                .thenReturn(1);

        controller.handleStart();

        controller.priceField.setText("5");
        controller.quantityField.setText("2");

        controller.handleAddItem();

        assertEquals("Total: 10.0", controller.totalLabel.getText());

        assertTrue(controller.priceField.isDisabled());
        assertTrue(controller.quantityField.isDisabled());
        assertTrue(controller.addItemButton.isDisabled());

        verify(cartService).updateCartTotal(1, 10.0);
    }

    @Test
    void handleAddItemInvalidInputShowsError() {
        controller.itemCountField.setText("1");

        when(cartService.saveCartRecord(anyInt(), anyDouble(), anyString()))
                .thenReturn(1);

        controller.handleStart();

        controller.priceField.setText("abc");
        controller.quantityField.setText("2");

        controller.handleAddItem();

        assertEquals("Invalid", controller.totalLabel.getText());
    }

    @Test
    void handleAddItemExceedsItemCount() {
        controller.itemCountField.setText("1");

        when(cartService.saveCartRecord(anyInt(), anyDouble(), anyString()))
                .thenReturn(1);

        controller.handleStart();

        controller.priceField.setText("10");
        controller.quantityField.setText("2");

        controller.handleAddItem(); // first item

        controller.priceField.setText("5");
        controller.quantityField.setText("1");

        controller.handleAddItem(); // exceeds item count

        assertEquals("Total: 20.0", controller.totalLabel.getText());
    }

    @Test
    void changeLanguageUpdatesUI() {
        controller.languageComboBox.getItems().addAll("English", "Finnish", "Arabic");
        controller.languageComboBox.setValue("English");

        controller.changeLanguage();

        assertEquals("Shopping Cart", controller.appName.getText());
        assertEquals("Start", controller.startButton.getText());
        assertEquals("Add item", controller.addItemButton.getText());
        assertEquals("Enter the amount of items", controller.itemCountField.getPromptText());
        assertEquals("Enter the price", controller.priceField.getPromptText());
        assertEquals("Enter the quantity", controller.quantityField.getPromptText());
    }

    @Test
    void changeLanguageToEnglishSetsLTR() {
        controller.languageComboBox.getItems().addAll("English", "Finnish", "Arabic");
        controller.languageComboBox.setValue("English");

        controller.changeLanguage();

        assertEquals(javafx.geometry.NodeOrientation.LEFT_TO_RIGHT, controller.root.getNodeOrientation());
    }

    @Test
    void changeLanguageToFinnish() {
        controller.languageComboBox.getItems().addAll("English", "Finnish");
        controller.languageComboBox.setValue("Finnish");

        controller.changeLanguage();

        assertEquals(javafx.geometry.NodeOrientation.LEFT_TO_RIGHT, controller.root.getNodeOrientation());
    }

    @Test
    void changeLanguageToArabicSetsRTL() {
        controller.languageComboBox.setValue("Arabic");

        controller.changeLanguage();

        assertEquals(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT,
                controller.root.getNodeOrientation());
    }

    @Test
    void changeLanguageToUnsupportedDefaultsToLTR() {
        controller.languageComboBox.setValue("xx");

        controller.changeLanguage();

        assertEquals(javafx.geometry.NodeOrientation.LEFT_TO_RIGHT,
                controller.root.getNodeOrientation());
    }
}