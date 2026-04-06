package org.example.service;

import org.example.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class LocalizationService {
    public static Map<String, String> getStrings(String language) {
        Map<String, String> map = new HashMap<>();

        String sql = "SELECT `key`, value FROM localization_strings WHERE language = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, language);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                map.put(rs.getString("key"), rs.getString("value"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return map;
    }
}