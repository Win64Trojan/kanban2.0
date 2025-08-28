package service;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

class InMemoryTaskManagerTest {

    private TaskManager manager;

    @BeforeEach
    public void creatingTestManager() {
        manager = Managers.getDefault();
    }

    @Test
    public void SubtaskCanNotBeEpicToItself() {
        Integer subtask = manager.createSubTask(new SubTask("", "",
                LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1), 0));
        Assertions.assertNull(manager.getSubTaskById(subtask));

    }

    @Test
    public void checkThatManagerCanCreateAndGiveSubtaskById() {
        Integer epic = manager.createEpic(new Epic("", ""));
        Integer subtask = manager.createSubTask(new SubTask("", "",
                LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1), epic));
        Assertions.assertNotNull(manager.getSubTaskById(subtask));
    }

    @Test
    public void checkThatManagerCanCreateAndGiveEpicById() {
        Integer epic = manager.createEpic(new Epic("", ""));
        Assertions.assertNotNull(manager.getEpicById(epic));
    }

    @Test
    public void checkThatManagerCanCreateAndGiveTaskById() {
        Integer task = manager.createTask(new Task("", "",
                LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1)));
        Assertions.assertNotNull(manager.getTaskById(task));
    }

    @Test
    public void checkImmutabilityOfEpicWhenCreatedValueChanged() {
        Integer created = manager.createEpic(new Epic("a", ""));
        Epic modifiedEpic = new Epic(manager.getEpicById(created).getTaskName(), manager.getEpicById(created).getTaskDesc());

        modifiedEpic.setTaskName("b");
        Assertions.assertNotEquals(modifiedEpic.getTaskName(), manager.getEpicById(created).getTaskName());
    }

    @Test
    public void checkImmutabilityOfEpicWhenSourceChanged() {
        Epic source = new Epic("a", "");
        Integer created = manager.createEpic(new Epic(source.getTaskName(), source.getTaskDesc()));
        source.setTaskName("b");
        Assertions.assertNotEquals(source.getTaskName(), manager.getEpicById(created).getTaskName());
    }

    @Test
    public void checkImmutabilityOfSubtaskWhenCreatedValueChanged() {
        Integer epic = manager.createEpic(new Epic("", ""));
        SubTask subtask = new SubTask("a", "",
                LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1), epic);

        Integer created = manager.createSubTask(subtask);
        manager.getSubTaskById(created).setTaskName("b");
        Assertions.assertEquals(subtask.getTaskName(), manager.getSubTaskById(created).getTaskName());
    }

    @Test
    public void checkImmutabilityOfSubtaskWhenSourceChanged() {
        Integer epic = manager.createEpic(new Epic("", ""));
        SubTask source = new SubTask("a", "",
                LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1), epic);
        Integer created = manager.createSubTask(source);
        source.setTaskName("b");
        Assertions.assertEquals(source.getTaskName(), manager.getSubTaskById(created).getTaskName());
    }

    @Test
    public void checkImmutabilityOfTaskWhenCreatedValueChanged() {
        Integer created = manager.createTask(new Task("a", "", LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1)));
        Task retrieved = manager.getTaskById(created);
        Task updated = new Task(retrieved.getTaskName(), retrieved.getTaskDesc(), retrieved.getStartTime(), retrieved.getDuration());
        updated.setTaskName("b");
        Assertions.assertNotEquals(updated.getTaskName(), manager.getTaskById(created).getTaskName());
    }

    @Test
    public void checkImmutabilityOfTaskWhenSourceChanged() {
        Task source = new Task("a", "",
                LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1));
        Integer created = manager.createTask(new Task(source.getTaskName(), source.getTaskDesc(), source.getStartTime(), source.getDuration()));
        source.setTaskName("b");
        Assertions.assertNotEquals(source.getTaskName(), manager.getTaskById(created).getTaskName());
    }
}