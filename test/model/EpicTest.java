package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {

    @Test
    void IdEqualsEpicTest() {
        Epic epic = new Epic("Task", "Task");
        Epic epic2 = new Epic("Task", "Task");

        assertEquals(epic, epic2);
    }
}