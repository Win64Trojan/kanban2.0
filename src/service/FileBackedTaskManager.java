package service;

import exceptions.ManagerSaveException;
import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskType;
import status.TaskStatus;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    private final File outputFile;

    private static final String title = "id,type,name,status,description,epic,startTime,duration";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    FileBackedTaskManager(File outputFile) {
        this.outputFile = outputFile;
        save();
    }

    public static void main(String[] args) {

        try {

            File fileSave = new File("saveTasks.csv");

            FileBackedTaskManager taskManager1 = new FileBackedTaskManager(fileSave);

            Task task = new Task("Купить кувшин", "Найти хороший магазин кувшинов", LocalDateTime.of(2025, Month.OCTOBER, 10, 21, 00), Duration.ofMinutes(10));
            taskManager1.createTask(task);

            Task task2 = new Task("Купить машину", "Найти хорошего консультанта", LocalDateTime.of(2025, Month.OCTOBER, 10, 21, 00), Duration.ofMinutes(10));
            taskManager1.createTask(task2);

            Epic epic = new Epic("Отпуск", "Найти вещи для отдыха");
            taskManager1.createEpic(epic);

            SubTask subTask = new SubTask("Лотерея", "Купить лотерейный билет", LocalDateTime.of(2025, Month.JUNE, 23, 10, 20), Duration.ofMinutes(1), epic.getId());
            taskManager1.createSubTask(subTask);

            SubTask subTask2 = new SubTask("Ботинки", "Почистить ботинки", LocalDateTime.of(2025, Month.JUNE, 23, 10, 30), Duration.ofMinutes(1), epic.getId());
            taskManager1.createSubTask(subTask2);


            FileBackedTaskManager taskManager2 = FileBackedTaskManager.loadFromFile(fileSave);

            if (taskManager1.getTasks().size() != taskManager2.getTasks().size()) {
                System.out.println("Размер задач не равны");
            }

            if (taskManager1.getSubTasks().size() != taskManager2.getSubTasks().size()) {
                System.out.println("Размер подзадач не равны");
            }

            if (taskManager1.getEpics().size() != taskManager2.getEpics().size()) {
                System.out.println("Размер Эпика не равны");
            }
            System.out.println(taskManager1.getPrioritizedTasks());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Task> getTasks() {
        save();
        return super.getTasks();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public Task getTaskById(Integer id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Integer createTask(Task newTask) {
        Integer id = super.createTask(newTask);
        save();
        return id;

    }

    @Override
    public Integer taskUpdate(Task newTask) {
        Integer id = super.taskUpdate(newTask);
        save();
        return id;
    }

    @Override
    public void deleteTaskById(Integer id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public List<Epic> getEpics() {
        save();
        return super.getEpics();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public Epic getEpicById(Integer id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Integer createEpic(Epic newEpic) {
        Integer id = super.createEpic(newEpic);
        save();
        return id;
    }

    @Override
    public Integer epicUpdate(Epic newEpic) {
        Integer id = super.epicUpdate(newEpic);
        save();
        return id;
    }

    @Override
    public void deleteEpicById(Integer id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public List<SubTask> getSubTasks() {
        save();
        return super.getSubTasks();
    }

    @Override
    public void removeAllSubTasks() {
        super.removeAllSubTasks();
        save();
    }

    @Override
    public SubTask getSubTaskById(Integer id) {
        SubTask subTask = super.getSubTaskById(id);
        save();
        return subTask;
    }

    @Override
    public Integer createSubTask(SubTask newSubtask) {
        Integer id = super.createSubTask(newSubtask);
        save();
        return id;
    }

    @Override
    public Integer subTaskUpdate(SubTask newSubtask) {
        Integer id = super.subTaskUpdate(newSubtask);
        save();
        return id;
    }

    @Override
    public void deleteSubTaskById(Integer id) {
        super.deleteSubTaskById(id);
        save();
    }

    @Override
    public List<SubTask> getSubTasksByEpicId(Integer epicId) {
        save();
        return super.getSubTasksByEpicId(epicId);
    }

    @Override
    public ArrayList<Task> getHistory() {
        save();
        return super.getHistory();
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        try {
            String[] lines = Files.readString(file.toPath()).split("\n");
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);

            for (String line : lines) {
                if (line.equals(title) || line.isBlank()) {
                    continue;
                }

                Task task = convertStringToTask(line);
                TaskType taskType = toEnum(task);

                switch (taskType) {

                    case TASK -> {
                        fileBackedTaskManager.createTask(task);
                    }
                    case EPIC -> {
                        fileBackedTaskManager.createEpic((Epic) task);
                    }
                    case SUBTASK -> {
                        fileBackedTaskManager.createSubTask((SubTask) task);
                    }

                    default -> {
                        throw new IllegalStateException("Неверное значение: " + taskType);
                    }
                }
            }
            return fileBackedTaskManager;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void save() {

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFile.toURI()), StandardCharsets.UTF_8)) {
            writer.write(title);

            for (Task task : tasks.values()) {
                writer.write(String.format("\n%s", convertTaskToString(task)));
            }

            for (Epic epic : epics.values()) {
                writer.write(String.format("\n%s", convertTaskToString(epic)));
            }

            for (SubTask subtask : subtasks.values()) {
                writer.write(String.format("\n%s", convertTaskToString(subtask)));
            }

        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
    }

    private String convertTaskToString(Task task) {
        TaskType taskType = toEnum(task);
        switch (taskType) {
            case EPIC, TASK -> {
                return String.format("%d,%s,%s,%s,%s,,%s,%d", task.getId(), taskType, task.getTaskName(), task.getTaskStatus(),
                        task.getTaskDesc(),
                        task.getStartTime() != null ? task.getStartTime().format(DATE_TIME_FORMATTER) : "",
                        task.getDuration().toMinutes());
            }

            case SUBTASK -> {
                SubTask subtask = (SubTask) task;
                return String.format("%d,%s,%s,%s,%s,%d,%s,%d", subtask.getId(), taskType, subtask.getTaskName(),
                        subtask.getTaskStatus(), subtask.getTaskDesc(), subtask.getEpicId(),
                        subtask.getStartTime().format(DATE_TIME_FORMATTER), subtask.getDuration().toMinutes());
            }
        }

        throw new IllegalStateException("Неверно значение: " + taskType);
    }

    private static Task convertStringToTask(String value) {
        String[] items = value.split(",");

        TaskType taskType = TaskType.valueOf(items[1]);
        int id = Integer.parseInt(items[0]);
        String taskName = items[2];
        String taskDesc = items[4];
        TaskStatus taskStatus = stringToStatus(items[3]);

        LocalDateTime startTime = null;
        if (!items[6].isBlank()) {
            startTime = LocalDateTime.parse(items[6], DATE_TIME_FORMATTER);
        }

        Duration duration = Duration.ofMinutes(Integer.parseInt(items[7]));

        switch (taskType) {
            case TASK -> {
                Task task = new Task(taskName, taskDesc, startTime, duration);
                task.setId(id);
                return task;
            }
            case EPIC -> {
                Epic epic = new Epic(taskName, taskDesc);
                epic.setId(id);
                return epic;
            }
            case SUBTASK -> {
                int epicId = Integer.parseInt(items[5]);
                SubTask subTask = new SubTask(taskName, taskDesc, startTime, duration, epicId);
                subTask.setId(id);
                return subTask;
            }

            default -> {
                throw new IllegalStateException("Неверное значение:" + taskType);
            }
        }
    }

    private static TaskStatus stringToStatus(String value) {
        TaskStatus taskStatus = TaskStatus.valueOf(value);

        switch (taskStatus) {
            case NEW, DONE, IN_PROGRESS -> {
                return taskStatus;
            }

            default -> {
                throw new IllegalStateException("Неверное значение: " + taskStatus);
            }
        }

    }

    private static TaskType toEnum(Task task) {
        if (task instanceof Epic) {
            return TaskType.EPIC;
        } else if (task instanceof SubTask) {
            return TaskType.SUBTASK;
        } else {
            return TaskType.TASK;
        }
    }
}
