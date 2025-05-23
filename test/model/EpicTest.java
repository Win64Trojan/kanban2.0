package model;

import org.junit.jupiter.api.Test;
import status.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;
class EpicTest {

    @Test
    void IdEqualsEpicTest(){
        Epic epic = new Epic("Task", "Task", 25, TaskStatus.NEW);
        Epic epic2 = new Epic("Task", "Task", 25, TaskStatus.NEW);

        assertEquals(epic, epic2);
    }
}