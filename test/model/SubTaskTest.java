package model;

import org.junit.jupiter.api.Test;
import status.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {

    @Test
    void IdEqualsSubtaskTest(){
        Epic epic = new Epic("Epic", "Epic", 25, TaskStatus.NEW);
        SubTask subTask = new SubTask("SubTask", "SubTask", 2, TaskStatus.NEW, 25);
        SubTask subTask1 = new SubTask("SubTask", "SubTask", 2, TaskStatus.NEW, 25);

        assertEquals(subTask.getEpicId(), subTask1.getEpicId());
    }
}