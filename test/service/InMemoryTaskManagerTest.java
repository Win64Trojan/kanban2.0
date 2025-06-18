package service;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import status.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class InMemoryTaskManagerTest {

    TaskManager taskManager;
    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
        historyManager.removeHistory();
    }

    @Test
    void getTasksTest() {
        Task task = new Task("TaskTest", "DescTest");
        HashMap<Integer, Task> tasks = new HashMap<>();
        tasks.put(1, task);
        ArrayList<Task> taskList = new ArrayList<>(tasks.values());

        taskManager.createTask(task);

        assertEquals(taskList, taskManager.getTasks());
    }

    @Test
    void removeAllTasksTest() {

        Task task1 = new Task("TaskTest", "DescTest");
        Task task2 = new Task("TaskTest2", "DescTest2");
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.removeAllTasks();

        assertNull(taskManager.getTasks());
    }

    @Test
    void getTaskByIdTest() {

        Task task = new Task("TaskTest", "DescTest");
        Task task2 = new Task("TaskTest2", "DescTest2");
        taskManager.createTask(task);
        taskManager.createTask(task2);

        assertEquals(task, taskManager.getTaskById(1));
        assertEquals(task2, taskManager.getTaskById(2));
    }

    @Test
    void createTaskTest() {

        Task task1 = new Task("TaskTest", "DescTest", 1, TaskStatus.NEW);
        Task task2 = new Task("TaskTest", "DescTest");

        taskManager.createTask(task2);

        assertEquals(task1, taskManager.getTaskById(1));
    }

    @Test
    void taskUpdateTest() {

        Task task1 = new Task("TaskTest", "DescTest");
        taskManager.createTask(task1);

        Task task2 = new Task("TaskTestNew", "DescTestNew", 1, TaskStatus.DONE);
        taskManager.taskUpdate(task2);

        assertEquals(task2, taskManager.getTaskById(1));

        task2.setTaskName("New Task");
        task2.setTaskDesc("New Desc");
        assertEquals(task2.getTaskName(), taskManager.getTaskById(1).getTaskName());
        assertEquals(task2.getTaskDesc(), taskManager.getTaskById(1).getTaskDesc());
    }

    @Test
    void deleteTaskByIdTest() {
        Task task1 = new Task("TaskTest", "DescTest");
        taskManager.createTask(task1);

        assertNotNull(taskManager.getTaskById(1));

        taskManager.deleteTaskById(1);

        assertNull(taskManager.getTaskById(1));
    }

    @Test
    void getEpicsTest() {
        Epic epic = new Epic("TaskTest", "DescTest");
        HashMap<Integer, Epic> epics = new HashMap<>();
        epics.put(1, epic);
        ArrayList<Epic> epicList = new ArrayList<>(epics.values());

        taskManager.createEpic(epic);

        assertEquals(epicList, taskManager.getEpics());
    }

    @Test
    void removeAllEpicsTest() {

        Epic epic = new Epic("EpicTest1", "DescTest1");
        Epic epic2 = new Epic("EpicTest2", "DescTest2");
        taskManager.createEpic(epic);
        taskManager.createEpic(epic2);

        assertNotNull(taskManager.getEpics());

        taskManager.removeAllEpics();

        assertNull(taskManager.getEpics());
    }

    @Test
    void getEpicByIdTest() {
        Epic epic = new Epic("EpicTest1", "DescTest1");
        taskManager.createEpic(epic);
        assertEquals(epic, taskManager.getEpicById(1));
    }

    @Test
    void createEpicTest() {
        Epic epic = new Epic("EpicTest2", "DescTest2");
        taskManager.createEpic(epic);
        assertEquals(epic, taskManager.getEpicById(1));
    }

    @Test
    void epicUpdateTest() {
        Epic epic = new Epic("EpicTest2", "DescTest2");
        Epic epic2 = new Epic("EpicTest3", "DescTest3", 1, TaskStatus.DONE);
        taskManager.createEpic(epic);
        taskManager.epicUpdate(epic2);
        assertEquals(epic2, taskManager.getEpicById(1));

        epic2.setId(2);
        assertEquals(epic2.getId(), 2);


    }

    @Test
    void deleteEpicByIdTest() {
        Epic epic = new Epic("EpicTest1", "DescTest1");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Subtask1", "Desc1", 1);
        taskManager.createSubTask(subTask);

        assertNotNull(taskManager.getEpicById(1));

        taskManager.deleteEpicById(1);

        assertNull(taskManager.getEpicById(1));
    }

    @Test
    void getSubTasksTest() {
        Epic epic = new Epic("EpicTest1", "DescTest1");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Subtask1", "Desc1", 1);
        taskManager.createSubTask(subTask);

        HashMap<Integer, SubTask> subtasks = new HashMap<>();
        subtasks.put(2, subTask);
        ArrayList<SubTask> subList = new ArrayList<>(subtasks.values());

        assertEquals(subList, taskManager.getSubTasks());
    }

    @Test
    void removeAllSubTasksTest() {
        Epic epic = new Epic("EpicTest1", "DescTest1");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Subtask1", "Desc1", 1);
        SubTask subTask2 = new SubTask("Subtask2", "Desc2", 1);
        taskManager.createSubTask(subTask);
        taskManager.createSubTask(subTask2);

        assertNotNull(taskManager.getSubTasks());
        taskManager.removeAllSubTasks();
        assertNull(taskManager.getSubTasks());
    }

    @Test
    void getSubTaskByIdTest() {
        Epic epic = new Epic("EpicTest1", "DescTest1");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Subtask1", "Desc1", 1);
        SubTask subTask2 = new SubTask("Subtask2", "Desc2", 1);
        taskManager.createSubTask(subTask);
        taskManager.createSubTask(subTask2);

        assertEquals(subTask, taskManager.getSubTaskById(2));
    }

    @Test
    void createSubTaskTest() {
        Epic epic = new Epic("EpicTest1", "DescTest1");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Subtask1", "Desc1", 1);
        SubTask subTask2 = new SubTask("Subtask2", "Desc2", 1);
        taskManager.createSubTask(subTask);
        taskManager.createSubTask(subTask2);

        assertNotNull(taskManager.getSubTaskById(2));
        assertEquals(subTask, taskManager.getSubTaskById(2));
    }

    @Test
    void subTaskUpdateTest() {
        Epic epic = new Epic("EpicTest1", "DescTest1");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Subtask1", "Desc1", 1);
        SubTask subTask2 = new SubTask("Subtask2", "Desc2", 1);
        taskManager.createSubTask(subTask);
        taskManager.createSubTask(subTask2);

        SubTask subTask3 = new SubTask("Subtask3", "Desc3", 2, TaskStatus.DONE, 1);
        taskManager.subTaskUpdate(subTask3);
        assertEquals(subTask3, taskManager.getSubTaskById(2));
    }

    @Test
    void deleteSubTaskByIdTest() {
        Epic epic = new Epic("EpicTest1", "DescTest1");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Subtask1", "Desc1", 1);
        SubTask subTask2 = new SubTask("Subtask2", "Desc2", 1);
        taskManager.createSubTask(subTask);
        taskManager.createSubTask(subTask2);

        assertNotNull(taskManager.getSubTaskById(2));
        taskManager.deleteSubTaskById(2);
        assertNull(taskManager.getSubTaskById(2));
    }

    @Test
    void getSubTasksByEpicIdTest() {
        Epic epic = new Epic("EpicTest1", "DescTest1");
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Subtask1", "Desc1", 1);
        SubTask subTask2 = new SubTask("Subtask2", "Desc2", 1);
        taskManager.createSubTask(subTask);
        taskManager.createSubTask(subTask2);

        HashMap<Integer, SubTask> subtasks = new HashMap<>();
        subtasks.put(2, subTask);
        subtasks.put(3, subTask2);
        ArrayList<SubTask> subList = new ArrayList<>(subtasks.values());

        assertEquals(subList, taskManager.getSubTasksByEpicId(1));
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
}