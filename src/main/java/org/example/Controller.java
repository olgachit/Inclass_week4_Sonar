package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.geometry.NodeOrientation;
import javafx.scene.layout.VBox;

import java.util.Locale;
import java.util.ResourceBundle;

public class Controller {
    @FXML private ComboBox<String> languageComboBox;
    @FXML private TextField itemCountField;
    @FXML private TextField priceField;
    @FXML private TextField quantityField;
    @FXML private Label itemLabel;
    @FXML private Label totalLabel;
    @FXML private VBox root;
    @FXML private Label appName;
    @FXML private Button startButton;
    @FXML private Button addItemButton;

    private ResourceBundle rb;
    private int itemCount;
    private int currentItem = 1;
    private double total = 0;

    @FXML
    public void initialize() {
        languageComboBox.setOnAction(event -> changeLanguage());
    }

    @FXML
    public void handleStart() {
        itemCount = Integer.parseInt(itemCountField.getText());
        currentItem = 1;
        total = 0;
        itemLabel.setText(rb.getString("label.item") + " " + currentItem);
        totalLabel.setText(rb.getString("total.cart") + " " + total);
    }

    @FXML
    public void handleAddItem() {
        if (currentItem > itemCount) {
            totalLabel.setText(rb.getString("total.cart") + " " + total);
            return;
        }
        try {
            double price = Double.parseDouble(priceField.getText());
            int quantity = Integer.parseInt(quantityField.getText());
            double itemTotal = price * quantity;
            totalLabel.setText(rb.getString("total.items") + " " + itemTotal);
            currentItem++;
            priceField.clear();
            quantityField.clear();

        } catch (NumberFormatException e) {
            totalLabel.setText(rb.getString("error.invalid"));
        }
    }

    private void changeLanguage() {
        String lang = languageComboBox.getValue();
        Locale locale;

        switch (lang) {
            case "English":  locale = new Locale("en", "US"); break;
            case "Finnish":  locale = new Locale("fi", "FI"); break;
            case "Swedish":  locale = new Locale("sv", "SE"); break;
            case "Japanese": locale = new Locale("ja", "JP"); break;
            case "Arabic":   locale = new Locale("ar", "AE"); break;
            default:         locale = new Locale("en", "US");
        }

        rb = ResourceBundle.getBundle("MessagesBundle", locale);

        itemCountField.setPromptText(rb.getString("prompt.items"));
        priceField.setPromptText(rb.getString("prompt.price"));
        quantityField.setPromptText(rb.getString("prompt.quantity"));
        itemLabel.setText(rb.getString("label.item") + " " + currentItem);
        totalLabel.setText(rb.getString("total.cart") + " " + total);
        appName.setText(rb.getString("app.name"));
        startButton.setText(rb.getString("button.start"));
        addItemButton.setText(rb.getString("button.add"));

        if (locale.getLanguage().equals("ar")) {
            root.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        } else {
            root.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        }
    }
}
