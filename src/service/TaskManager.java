package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;

public interface TaskManager {
    ArrayList<Task> getTasks();

    void removeAllTasks();

    Task getTaskById(Integer id);

    Integer createTask(Task newTask);

    Integer taskUpdate(Task newTask);

    void deleteTaskById(Integer id);

    ArrayList<Epic> getEpics();

    void removeAllEpics();

    Epic getEpicById(Integer id);

    Integer createEpic(Epic newEpic);

    Integer epicUpdate(Epic newEpic);

    void deleteEpicById(Integer id);

    ArrayList<SubTask> getSubTasks();

    void removeAllSubTasks();

    SubTask getSubTaskById(Integer id);

    Integer createSubTask(SubTask newSubtask);

    Integer subTaskUpdate(SubTask newSubtask);

    void deleteSubTaskById(Integer id);

    ArrayList<SubTask> getSubTasksByEpicId(Integer epicId);

    ArrayList<Task> getHistory();
}
