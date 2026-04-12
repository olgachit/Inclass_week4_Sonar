package org.example.service;

import org.example.DatabaseConnection;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LocalizationServiceTest {

    @Test
    void getStringsReturnsMappedValues() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        try (MockedStatic<DatabaseConnection> dbMock = mockStatic(DatabaseConnection.class)) {

            dbMock.when(DatabaseConnection::getConnection).thenReturn(conn);
            when(conn.prepareStatement(anyString())).thenReturn(stmt);
            when(stmt.executeQuery()).thenReturn(rs);

            when(rs.next()).thenReturn(true, true, false);

            when(rs.getString("key"))
                    .thenReturn("app.name", "button.start");

            when(rs.getString("value"))
                    .thenReturn("Shopping App", "Start");

            Map<String, String> result =
                    LocalizationService.getStrings("en");

            assertEquals(2, result.size());
            assertEquals("Shopping App", result.get("app.name"));
            assertEquals("Start", result.get("button.start"));

            verify(stmt).setString(1, "en");
        }
    }

    @Test
    void getStringsReturnsEmptyMapWhenNoResults() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        try (MockedStatic<DatabaseConnection> dbMock = mockStatic(DatabaseConnection.class)) {

            dbMock.when(DatabaseConnection::getConnection).thenReturn(conn);
            when(conn.prepareStatement(anyString())).thenReturn(stmt);
            when(stmt.executeQuery()).thenReturn(rs);

            when(rs.next()).thenReturn(false);

            Map<String, String> result =
                    LocalizationService.getStrings("fi");

            assertTrue(result.isEmpty());
        }
    }

    @Test
    void getStringsHandlesSQLException() {

        try (MockedStatic<DatabaseConnection> dbMock = mockStatic(DatabaseConnection.class)) {

            dbMock.when(DatabaseConnection::getConnection)
                    .thenThrow(new SQLException("DB failure"));

            Map<String, String> result =
                    LocalizationService.getStrings("en");
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }
}