package service;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class InMemoryHistoryManagerTest {


    TaskManager taskManager;
    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
        historyManager.removeHistory();

    }

    @Test
    void addTest() {
        Epic epic = new Epic("EpicTest1", "DescTest1");
        Task task = new Task("Task1", "Desc1", LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1));
        SubTask subTask = new SubTask("Subtask1", "Desc1", LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1), 1);

        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask);
        taskManager.createTask(task);

        for (int i = 0; i < 12; i++) {
            taskManager.getEpicById(1);
            taskManager.getSubTaskById(2);
            taskManager.getTaskById(3);
        }

        ArrayList<Task> tasks = taskManager.getHistory();

        assertEquals(1, tasks.size());
        assertNotEquals(12, tasks.size());

    }


}