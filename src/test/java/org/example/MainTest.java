package org.example;

import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.testfx.framework.junit5.ApplicationTest;


class MainTest extends ApplicationTest {

    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        new Main().start(stage);
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