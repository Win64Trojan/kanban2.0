package service;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

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
        Task task = new Task("Task1", "Desc1");
        SubTask subTask = new SubTask("Subtask1", "Desc1", 1);

        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask);
        taskManager.createTask(task);

        for (int i = 0; i < 12; i++) {
            taskManager.getEpicById(1);
            taskManager.getSubTaskById(2);
            taskManager.getTaskById(3);
        }

        ArrayList<Task> tasks = taskManager.getHistory();

        assertEquals(3, tasks.size());
        assertNotEquals(12, tasks.size());

    }

    @Test
    void getHistoryTest() {
        Epic epic = new Epic("EpicTest1", "DescTest1");
        Task task = new Task("Task1", "Desc1");
        SubTask subTask = new SubTask("Subtask1", "Desc1", 1);

        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask);
        taskManager.createTask(task);

        ArrayList<Task> historyTest = new ArrayList<>();
        historyTest.add(epic);
        historyTest.add(subTask);
        historyTest.add(task);

        taskManager.getEpicById(1);
        taskManager.getSubTaskById(2);
        taskManager.getTaskById(3);

        assertEquals(historyTest, taskManager.getHistory());
    }

    @Test
    void addAndRemoveHistoryTest() {

        Epic epic = new Epic("EpicTest1", "DescTest1");
        Task task = new Task("Task1", "Desc1");
        SubTask subTask = new SubTask("Subtask1", "Desc1", 1);

        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask);
        taskManager.createTask(task);

        historyManager.add(taskManager.getEpicById(1));
        historyManager.add(taskManager.getTaskById(3));
        historyManager.add(taskManager.getTaskById(3));
        historyManager.add(taskManager.getSubTaskById(2));
        historyManager.add(taskManager.getEpicById(1));
        historyManager.add(taskManager.getSubTaskById(2));
        historyManager.add(taskManager.getTaskById(3));


        historyManager.remove(1);
        ArrayList<Task> historyTest;
        historyTest = historyManager.getHistory();
        assertFalse(historyTest.contains(epic));

        historyManager.removeHistory();
        assertNull(historyManager.getHistory());

    }


}