package service;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest {

    @Test
    void shouldStoreOnlyTitleIfNoTasksAdded() {
        try {
            File file = File.createTempFile("test", "csv");
            FileBackedTaskManager taskManager = new FileBackedTaskManager(file);

            String[] lines = Files.readString(file.toPath()).split("\n");
            assertEquals(lines.length, 1);
            assertEquals(lines[0], "id,type,name,status,description,epic");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldStoreTasksToFile() {
        try {
            File file = File.createTempFile("test", "csv");
            FileBackedTaskManager taskManager = new FileBackedTaskManager(file);

            Task task = new Task("Подстричь волосы", "Взять ножницы");
            taskManager.createTask(task);

            Epic epic = new Epic("Купить машину", "Найти консультанта");
            taskManager.createEpic(epic);

            SubTask subTask = new SubTask("Поиск консультанта", "Заплатить консультанту", epic.getId());
            taskManager.createSubTask(subTask);

            String[] lines = Files.readString(file.toPath()).split("\n");

            assertEquals(lines.length, 4);

            assertEquals(lines[0], "id,type,name,status,description,epic");
            assertEquals(lines[1], "1,TASK,Подстричь волосы,NEW,Взять ножницы,");
            assertEquals(lines[2], "2,EPIC,Купить машину,NEW,Найти консультанта,");
            assertEquals(lines[3], "3,SUBTASK,Поиск консультанта,NEW,Заплатить консультанту,2");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldLoadNoTasksFromEmptyFile() {
        try {
            File file = File.createTempFile("test", "csv");
            FileBackedTaskManager taskManager = FileBackedTaskManager.loadFromFile(file);

            assertEquals(taskManager.getSubTasks().size(), 0);
            assertEquals(taskManager.getTasks().size(), 0);
            assertEquals(taskManager.getEpics().size(), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldLoadTasksFromFile() {
        try {
            File file = File.createTempFile("test", "csv");

            try (FileWriter writer = new FileWriter(file)) {

                writer.write("""
                        id,type,name,status,description,epic
                        1,TASK,Подстричь волосы,NEW,Взять ножницы,
                        2,EPIC,Купить машину,NEW,Найти консультанта,
                        3,SUBTASK,Поиск консультанта,NEW,Заплатить консультанту,2""");
            }


            FileBackedTaskManager taskManager = FileBackedTaskManager.loadFromFile(file);


            assertEquals(1, taskManager.getTasks().size());
            assertEquals(1, taskManager.getEpics().size());
            assertEquals(1, taskManager.getSubTasks().size());

            Task expectedTask = new Task("Подстричь волосы", "Взять ножницы");
            expectedTask.setId(1);

            Epic expectedEpic = new Epic("Купить машину", "Найти консультанта");
            expectedEpic.setId(2);

            SubTask expectedSubTask = new SubTask("Поиск консультанта", "Заплатить консультанту",
                    expectedEpic.getId());
            expectedSubTask.setId(3);

            assertEquals(expectedTask, taskManager.getTasks().getFirst());
            assertEquals(expectedEpic, taskManager.getEpics().getFirst());
            assertEquals(expectedSubTask, taskManager.getSubTasks().getFirst());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}