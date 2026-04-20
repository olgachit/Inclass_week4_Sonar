package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.geometry.NodeOrientation;
import javafx.scene.layout.VBox;
import org.example.service.CartService;
import org.example.service.LocalizationService;
import java.util.Map;

public class Controller {
    @FXML ComboBox<String> languageComboBox;
    @FXML TextField itemCountField;
    @FXML TextField priceField;
    @FXML TextField quantityField;
    @FXML Label itemLabel;
    @FXML Label totalLabel;
    @FXML VBox root;
    @FXML Label appName;
    @FXML Button startButton;
    @FXML Button addItemButton;

    private Map<String, String> rb;
    private CartService cartService;
    private int cartId;
    private String currentLanguage = "en";
    private int itemCount;
    private int currentItem = 1;
    private double total = 0;
    private String totalCartKey = "total.cart";
    private String labelItemKey = "label.item";

    public Controller(CartService cartService, Map<String, String> rb) {
        this.cartService = cartService;
        this.rb = rb;
    }

    @FXML
    public void initialize() {
        languageComboBox.setOnAction(event -> changeLanguage());
        languageComboBox.setValue("English");
        changeLanguage();
    }

    @FXML
    public void handleStart() {
        priceField.setDisable(false);
        quantityField.setDisable(false);
        addItemButton.setDisable(false);
        itemCount = Integer.parseInt(itemCountField.getText());
        currentItem = 1;
        total = 0;
        cartId = cartService.saveCartRecord(itemCount, total, currentLanguage);
        itemLabel.setText(rb.get(labelItemKey) + " " + currentItem);
        totalLabel.setText(rb.get(totalCartKey) + " " + total);
    }

    @FXML
    public void handleAddItem() {
        if (currentItem > itemCount) {
            totalLabel.setText(rb.get(totalCartKey) + " " + total);
            return;
        }
        try {
            double price = Double.parseDouble(priceField.getText());
            int quantity = Integer.parseInt(quantityField.getText());
            double itemTotal = price * quantity;

            total += itemTotal;
            cartService.saveCartItem(cartId, currentItem, price, quantity);
            currentItem++;

            if (currentItem > itemCount) {
                cartService.updateCartTotal(cartId, total);
                totalLabel.setText(rb.get(totalCartKey) + " " + total);
                disableInputs();
            } else {
                totalLabel.setText(rb.get("total.items") + " " + itemTotal);
                itemLabel.setText(rb.get(labelItemKey) + " " + currentItem);
            }

            priceField.clear();
            quantityField.clear();

        } catch (NumberFormatException e) {
            totalLabel.setText(rb.get("error.invalid"));
        }
    }

    void changeLanguage() {
        String lang = languageComboBox.getValue();

        switch (lang) {
            case "English":  currentLanguage = "en"; break;
            case "Finnish":  currentLanguage = "fi"; break;
            case "Swedish":  currentLanguage = "sv"; break;
            case "Japanese": currentLanguage = "ja"; break;
            case "Arabic":   currentLanguage = "ar"; break;
            default:         currentLanguage = "en";
        }

        rb = LocalizationService.getStrings(currentLanguage);

        itemCountField.setPromptText(rb.get("prompt.items"));
        priceField.setPromptText(rb.get("prompt.price"));
        quantityField.setPromptText(rb.get("prompt.quantity"));
        itemLabel.setText(rb.get(labelItemKey) + " " + currentItem);
        totalLabel.setText(rb.get(totalCartKey) + " " + total);
        appName.setText(rb.get("app.name"));
        startButton.setText(rb.get("button.start"));
        addItemButton.setText(rb.get("button.add"));

        if (currentLanguage.equals("ar")) {
            root.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        } else {
            root.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        }
    }

    private void disableInputs() {
        priceField.setDisable(true);
        quantityField.setDisable(true);
        addItemButton.setDisable(true);
    }

    void setResourceBundle(Map<String, String> rb) {
        this.rb = rb;
    }
}
