package org.example.service;

import org.example.DatabaseConnection;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceTest {
    private static final String ERROR = "DB error";

    @Test
    void saveCartRecordReturnsGeneratedId() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet keys = mock(ResultSet.class);

        try (MockedStatic<DatabaseConnection> dbMock = mockStatic(DatabaseConnection.class)) {

            dbMock.when(DatabaseConnection::getConnection).thenReturn(conn);

            when(conn.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                    .thenReturn(stmt);

            when(stmt.getGeneratedKeys()).thenReturn(keys);
            when(keys.next()).thenReturn(true);
            when(keys.getInt(1)).thenReturn(42);

            CartService service = new CartService();

            int result = service.saveCartRecord(3, 10.0, "en");

            assertEquals(42, result);

            verify(stmt).setInt(1, 3);
            verify(stmt).setDouble(2, 10.0);
            verify(stmt).setString(3, "en");
            verify(stmt).executeUpdate();
        }
    }

    @Test
    void saveCartRecordReturnsMinusOneWhenNoKeys() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet keys = mock(ResultSet.class);

        try (MockedStatic<DatabaseConnection> dbMock = mockStatic(DatabaseConnection.class)) {

            dbMock.when(DatabaseConnection::getConnection).thenReturn(conn);

            when(conn.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                    .thenReturn(stmt);

            when(stmt.getGeneratedKeys()).thenReturn(keys);
            when(keys.next()).thenReturn(false);

            CartService service = new CartService();

            int result = service.saveCartRecord(2, 5.0, "fi");

            assertEquals(-1, result);
        }
    }

    @Test
    void saveCartItemExecutesInsertWithSubtotal() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);

        try (MockedStatic<DatabaseConnection> dbMock = mockStatic(DatabaseConnection.class)) {

            dbMock.when(DatabaseConnection::getConnection).thenReturn(conn);

            when(conn.prepareStatement(anyString())).thenReturn(stmt);

            CartService service = new CartService();

            service.saveCartItem(1, 2, 10.0, 3); // subtotal = 30

            verify(stmt).setInt(1, 1);
            verify(stmt).setInt(2, 2);
            verify(stmt).setDouble(3, 10.0);
            verify(stmt).setInt(4, 3);
            verify(stmt).setDouble(5, 30.0);

            verify(stmt).executeUpdate();
        }
    }

    @Test
    void updateCartTotalExecutesUpdateQuery() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement stmt = mock(PreparedStatement.class);

        try (MockedStatic<DatabaseConnection> dbMock = mockStatic(DatabaseConnection.class)) {

            dbMock.when(DatabaseConnection::getConnection).thenReturn(conn);

            when(conn.prepareStatement(anyString())).thenReturn(stmt);

            CartService service = new CartService();

            service.updateCartTotal(5, 99.99);

            verify(stmt).setDouble(1, 99.99);
            verify(stmt).setInt(2, 5);
            verify(stmt).executeUpdate();
        }
    }

    @Test
    void saveCartRecordHandlesSQLException() throws Exception {

        Connection conn = mock(Connection.class);

        try (MockedStatic<DatabaseConnection> dbMock = mockStatic(DatabaseConnection.class)) {

            dbMock.when(DatabaseConnection::getConnection).thenReturn(conn);
            when(conn.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                    .thenThrow(new SQLException(ERROR));

            CartService service = new CartService();

            int result = service.saveCartRecord(1, 1.0, "en");

            assertEquals(-1, result);
        }
    }

    @Test
    void saveCartItemHandlesSQLException() throws Exception {
        Connection conn = mock(Connection.class);

        try (MockedStatic<DatabaseConnection> dbMock = mockStatic(DatabaseConnection.class)) {

            dbMock.when(DatabaseConnection::getConnection).thenReturn(conn);
            when(conn.prepareStatement(anyString())).thenThrow(new SQLException(ERROR));

            CartService service = new CartService();

            assertDoesNotThrow(() -> service.saveCartItem(1, 1, 10.0, 1));
        }
    }

    @Test
    void updateCartTotalHandlesSQLException() throws Exception {
        Connection conn = mock(Connection.class);

        try (MockedStatic<DatabaseConnection> dbMock = mockStatic(DatabaseConnection.class)) {

            dbMock.when(DatabaseConnection::getConnection).thenReturn(conn);
            when(conn.prepareStatement(anyString())).thenThrow(new SQLException(ERROR));

            CartService service = new CartService();

            assertDoesNotThrow(() -> service.updateCartTotal(1, 50.0));
        }
    }
}