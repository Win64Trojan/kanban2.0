package model;

import org.junit.jupiter.api.Test;
import status.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void IdEqualsTaskTest(){
        Task task = new Task("Task", "Task", 25, TaskStatus.NEW);
        Task task2 = new Task("Task", "Task", 25, TaskStatus.NEW);

        assertEquals(task, task2);
    }
}