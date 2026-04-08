package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void testAddMe() {
        assertEquals(16, Main.addMe(12, 4));
    }
}