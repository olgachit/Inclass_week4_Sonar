package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DatabaseConnectionTest {

    @Test
    void getConnectionReturnsConnection() throws Exception {
        Connection mockConn = mock(Connection.class);

        try (MockedStatic<DriverManager> driverMock = mockStatic(DriverManager.class)) {

            driverMock.when(() ->
                    DriverManager.getConnection(anyString(), anyString(), anyString())
            ).thenReturn(mockConn);

            Connection result = DatabaseConnection.getConnection();

            assertNotNull(result);
            assertEquals(mockConn, result);
        }
    }

    @Test
    void staticInitializerHandlesDotenvException() throws Exception {

        try (MockedStatic<Dotenv> dotenvMock = mockStatic(Dotenv.class)) {

            dotenvMock.when(Dotenv::configure)
                    .thenThrow(new RuntimeException("fail"));

            Class<?> clazz = Class.forName("org.example.DatabaseConnection");

            var field = clazz.getDeclaredField("dotenv");
            field.setAccessible(true);

            Object value = field.get(null);

            assertNull(value);
        }
    }
}