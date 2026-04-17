package org.example;

import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MainTest extends ApplicationTest {

    private Stage stage;
    private MockedStatic<org.example.service.LocalizationService> mock;

    @Override
    public void init() throws Exception {
        mock = mockStatic(org.example.service.LocalizationService.class);
        mock.when(() -> org.example.service.LocalizationService.getStrings(anyString()))
                .thenReturn(Map.of("app.name", "Olga Chitembo/Shopping Cart"));
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        new Main().start(stage); // now safe ✅
    }

    @Override
    public void stop() throws Exception {
        mock.close(); // cleanup
    }

    @Test
    void testStageIsShowing() {
        assertTrue(stage.isShowing());
    }

    @Test
    void testWindowTitle() {
        assertEquals("Olga Chitembo/Shopping Cart", stage.getTitle());
    }
}