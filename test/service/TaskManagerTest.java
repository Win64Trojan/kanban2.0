package service;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import status.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    @Test
    void addNewTask() {
        assertDoesNotThrow(() -> {
            Task task = new Task("Test addNewTask", "Test addNewTask description", LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1));
            taskManager.createTask(task);
            int taskId = task.getId();

            final Task savedTask = taskManager.getTaskById(taskId);

            assertNotNull(savedTask, "Задача не найдена.");
            assertEquals(task, savedTask, "Задачи не совпадают.");

            final List<Task> tasks = taskManager.getTasks();

            assertNotNull(tasks, "Задачи не возвращаются.");
            assertEquals(1, tasks.size(), "Неверное количество задач.");
            assertEquals(task, tasks.getFirst(), "Задачи не совпадают.");
        });
    }

    @Test
    void addNewEpic() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        taskManager.createEpic(epic);
        int epicId = epic.getId();

        final Epic savedTask = taskManager.getEpicById(epicId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(epic, savedTask, "Задачи не совпадают.");

        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.getFirst(), "Задачи не совпадают.");
    }

    @Test
    void addNewSubTask() {
        assertDoesNotThrow(() -> {
            Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
            taskManager.createEpic(epic);
            Integer epicId = epic.getId();

            SubTask subTask = new SubTask("Test addNewSubTAsk", "Test addNewSubTAsk description", LocalDateTime.of(2024, Month.JUNE, 22, 23, 07), Duration.ofMinutes(1), epicId);
            taskManager.createSubTask(subTask);

            final SubTask savedTask = taskManager.getSubTaskById(subTask.getId());

            assertNotNull(savedTask, "Задача не найдена.");
            assertEquals(subTask, savedTask, "Задачи не совпадают.");

            final List<SubTask> subTasks = taskManager.getSubTasks();

            assertNotNull(subTasks, "Задачи не возвращаются.");
            assertEquals(1, subTasks.size(), "Неверное количество задач.");
            assertEquals(subTask, subTasks.getFirst(), "Задачи не совпадают.");
        });
    }

    @Test
    void shouldReturnEpicById() {
        Epic epic = new Epic("test", "test");
        taskManager.createEpic(epic);

        Epic epic2 = taskManager.getEpicById(epic.getId());

        Assertions.assertEquals(epic, epic2);
    }

    @Test
    void shouldReturnTaskById() {
        assertDoesNotThrow(() -> {
            Task task = new Task("test", "test", LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1));
            taskManager.createTask(task);

            Task task2 = taskManager.getTaskById(task.getId());

            Assertions.assertEquals(task, task2);
        });
    }

    @Test
    void shouldReturnSubTaskById() {
        assertDoesNotThrow(() -> {
            Epic epic = new Epic("test", "test");
            taskManager.createEpic(epic);

            SubTask subTask = new SubTask("test", "test", LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1), 1);
            taskManager.createSubTask(subTask);

            SubTask subTask2 = taskManager.getSubTaskById(subTask.getId());

            Assertions.assertEquals(subTask, subTask2);
        });
    }

    @Test
    void shouldUpdateIdsOnCreate() {
        Epic epic = new Epic("test", "test");
        epic.setId(0);
        taskManager.createEpic(epic);

        Epic epic2 = new Epic("test", "test");
        epic.setId(0);
        taskManager.createEpic(epic);

        Assertions.assertNotEquals(epic.getId(), epic2.getId());
    }

    @Test
    void shouldEqualTaskFieldsBeforeAndAfterCreate() {
        assertDoesNotThrow(() -> {
            Task oldTask = new Task("test", "test", LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1));
            taskManager.createTask(oldTask);

            Task newTask = taskManager.getTaskById(oldTask.getId());


            Assertions.assertEquals(oldTask.getTaskName(), newTask.getTaskName());
            Assertions.assertEquals(oldTask.getTaskDesc(), newTask.getTaskDesc());
            Assertions.assertEquals(oldTask.getTaskStatus(), newTask.getTaskStatus());
            Assertions.assertEquals(oldTask.getClass(), newTask.getClass());
        });
    }

    @Test
    void shouldNotSubtasksIdEqualToEpicId() {
        assertDoesNotThrow(() -> {
            Epic epic = new Epic("test", "test");
            taskManager.createEpic(epic);

            SubTask subTask = new SubTask("test", "test",
                    LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1), 1);
            taskManager.createSubTask(subTask);

            List<SubTask> subTasksBeforeUpdate = taskManager.getSubTasks();

            subTask.setId(1);
            taskManager.subTaskUpdate(subTask);

            List<SubTask> subTasksAfterUpdate = taskManager.getSubTasks();

            Assertions.assertArrayEquals(subTasksBeforeUpdate.toArray(), subTasksAfterUpdate.toArray());
        });
    }

    @Test
    void shouldNotEpicContainRemovedTaskId() {
        assertDoesNotThrow(() -> {
            Epic epic = new Epic("test", "test");
            taskManager.createEpic(epic);

            SubTask subTask = new SubTask("test", "test", LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1), 1);
            taskManager.createSubTask(subTask);

            taskManager.deleteSubTaskById(subTask.getId());

            List<SubTask> subTasksList = taskManager.getSubTasksByEpicId(epic.getId());
            Assertions.assertEquals(subTasksList.size(), 0);
        });
    }

    @Test
    void shouldReturnTrueWhenTaskIncludesAnotherTask() {
        Task task1 = new Task("task1", "task1", LocalDateTime.of(2024, 06, 19, 15, 00), Duration.ofMinutes(10));

        Task task2 = new Task("task2", "task2", LocalDateTime.of(2024, 06, 19, 15, 00), Duration.ofMinutes(2));

        boolean isIntersected = taskManager.isIntersectedTasks(task1, task2);

        Assertions.assertTrue(isIntersected);

        isIntersected = taskManager.isIntersectedTasks(task2, task1);

        Assertions.assertTrue(isIntersected);
    }

    @Test
    void shouldReturnTrueWhenTaskInctersectsAnotherTask() {
        Task task1 = new Task("task1", "task1", LocalDateTime.of(2024, 06, 19, 15, 00), Duration.ofMinutes(10));

        Task task2 = new Task("task2", "task2", LocalDateTime.of(2024, 06, 19, 15, 05), Duration.ofMinutes(15));

        boolean isIntersected = taskManager.isIntersectedTasks(task1, task2);

        Assertions.assertTrue(isIntersected);

        isIntersected = taskManager.isIntersectedTasks(task2, task1);

        Assertions.assertTrue(isIntersected);
    }

    @Test
    void shouldBeFalseWhenTasksRangesAreNotIntersects() {
        Task task1 = new Task("task1", "task1", LocalDateTime.of(2024, 06, 19, 15, 00), Duration.ofMinutes(10));

        Task task2 = new Task("task2", "task2", LocalDateTime.of(2024, 06, 19, 15, 10), Duration.ofMinutes(15));

        boolean isIntersected = taskManager.isIntersectedTasks(task1, task2);

        Assertions.assertFalse(isIntersected);

        isIntersected = taskManager.isIntersectedTasks(task2, task1);

        Assertions.assertFalse(isIntersected);
    }

    @Test
    void shouldReturnNewIfAllSubTasksOfEpicAreNew() {
        assertDoesNotThrow(() -> {
            Epic epic = new Epic("test", "test");
            taskManager.createEpic(epic);
            int epicId = epic.getId();

            SubTask subTask = new SubTask("test", "test", LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1), epicId);
            taskManager.createSubTask(subTask);

            SubTask subTask2 = new SubTask("test", "test", LocalDateTime.of(2024, Month.JUNE, 19, 10, 50), Duration.ofMinutes(1), epicId);
            taskManager.createSubTask(subTask2);

            epic = taskManager.getEpicById(epicId);

            assertEquals(epic.getTaskStatus(), TaskStatus.NEW);
        });
    }

    @Test
    void shouldReturnDoneIfAllSubTasksOfEpicAreDone() {
        assertDoesNotThrow(() -> {
            Epic epic = new Epic("test", "test");
            taskManager.createEpic(epic);
            Integer epicId = epic.getId();

            SubTask subTask = new SubTask("test", "test", LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1), epicId);
            subTask.setTaskStatus(TaskStatus.DONE);
            taskManager.createSubTask(subTask);

            SubTask subTask2 = new SubTask("test", "test", LocalDateTime.of(2024, Month.JUNE, 19, 10, 50), Duration.ofMinutes(1), epicId);
            subTask2.setTaskStatus(TaskStatus.DONE);
            taskManager.createSubTask(subTask2);

            epic = taskManager.getEpicById(epicId);
            assertEquals(epic.getTaskStatus(), TaskStatus.DONE);
        });
    }

    @Test
    void shouldReturnInProgressIfSubTasksOfEpicInNewAndDoneStatus() {
        assertDoesNotThrow(() -> {
            Epic epic = new Epic("test", "test");
            taskManager.createEpic(epic);
            int epicId = epic.getId();

            SubTask subTask = new SubTask("test", "test", LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1), epicId);
            subTask.setTaskStatus(TaskStatus.NEW);
            taskManager.createSubTask(subTask);

            SubTask subTask2 = new SubTask("test", "test", LocalDateTime.of(2024, Month.JUNE, 19, 10, 50), Duration.ofMinutes(1), epicId);
            subTask2.setTaskStatus(TaskStatus.DONE);
            taskManager.createSubTask(subTask2);


            epic = taskManager.getEpicById(epicId);

            assertEquals(epic.getTaskStatus(), TaskStatus.IN_PROGRESS);
        });
    }

    @Test
    void shouldReturnInProgressIfSubTasksOfEpicInProgressStatus() {
        assertDoesNotThrow(() -> {
            Epic epic = new Epic("test", "test");
            taskManager.createEpic(epic);
            int epicId = epic.getId();

            SubTask subTask = new SubTask("test", "test", LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1), epicId);
            subTask.setTaskStatus(TaskStatus.IN_PROGRESS);
            taskManager.createSubTask(subTask);

            SubTask subTask2 = new SubTask("test", "test", LocalDateTime.of(2024, Month.JUNE, 19, 10, 50), Duration.ofMinutes(1), epicId);
            subTask2.setTaskStatus(TaskStatus.IN_PROGRESS);
            taskManager.createSubTask(subTask2);

            epic = taskManager.getEpicById(epicId);

            assertEquals(epic.getTaskStatus(), TaskStatus.IN_PROGRESS);
        });
    }

    @Test
    void shouldThrowNothingOnNoIntersectedTaskTimes() {
        assertDoesNotThrow(() -> {
            Task task1 = new Task("test", "test", LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(20));
            Task task2 = new Task("test", "test", LocalDateTime.of(2024, Month.JUNE, 19, 10, 50), Duration.ofMinutes(1));
            taskManager.createTask(task1);
            taskManager.createTask(task2);
        });
    }
}