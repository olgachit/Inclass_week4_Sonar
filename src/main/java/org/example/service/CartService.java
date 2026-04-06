package org.example.service;

import org.example.DatabaseConnection;

import java.sql.*;

public class CartService {

    public int saveCartRecord(int totalItems, double totalCost, String language) {
        String sql = "INSERT INTO cart_records (total_items, total_cost, language) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, totalItems);
            stmt.setDouble(2, totalCost);
            stmt.setString(3, language);

            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1); // return generated cart_record_id
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public void saveCartItem(int cartId, int itemNumber, double price, int quantity) {
        String sql = "INSERT INTO cart_items (cart_record_id, item_number, price, quantity, subtotal) VALUES (?, ?, ?, ?, ?)";

        double subtotal = price * quantity;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cartId);
            stmt.setInt(2, itemNumber);
            stmt.setDouble(3, price);
            stmt.setInt(4, quantity);
            stmt.setDouble(5, subtotal);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateCartTotal(int cartId, double total) {
        String sql = "UPDATE cart_records SET total_cost = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, total);
            stmt.setInt(2, cartId);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}