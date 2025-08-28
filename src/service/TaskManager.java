package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    List<Task> getTasks();

    void removeAllTasks();

    Task getTaskById(Integer id);

    Integer createTask(Task newTask);

    Integer taskUpdate(Task newTask);

    void deleteTaskById(Integer id);

    List<Epic> getEpics();

    void removeAllEpics();

    Epic getEpicById(Integer id);

    Integer createEpic(Epic newEpic);

    Integer epicUpdate(Epic newEpic);

    void deleteEpicById(Integer id);

    List<SubTask> getSubTasks();

    void removeAllSubTasks();

    SubTask getSubTaskById(Integer id);

    Integer createSubTask(SubTask newSubtask);

    Integer subTaskUpdate(SubTask newSubtask);

    void deleteSubTaskById(Integer id);

    List<SubTask> getSubTasksByEpicId(Integer epicId);

    ArrayList<Task> getHistory();

    List<Task> getPrioritizedTasks();

    boolean isIntersectedTasks(Task t1, Task t2);
}
