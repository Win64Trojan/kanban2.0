package service;

import model.Epic;
import model.SubTask;
import model.Task;
import status.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {

    private Integer ids = 0;

    private final HashMap<Integer, Task> tasks = new HashMap<>();

    private final HashMap<Integer, Epic> epics = new HashMap<>();

    private final HashMap<Integer, SubTask> subtasks = new HashMap<>();

    private final HistoryManager historyManagers = Managers.getDefaultHistory();


    /// Для model.Task
    @Override
    public ArrayList<Task> getTasks() {
        if (tasks.isEmpty()) {
            return null;
        }
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
    }

    @Override
    public Task getTaskById(Integer id) {
        Task t = tasks.get(id);
        if (t == null) {
            return null;
        }
        historyManagers.add(t);
        return tasks.get(id);
    }

    @Override
    public Integer createTask(Task newTask) {
        int id = ++ids;
        newTask.setId(id);
        tasks.put(id, newTask);
        return id;
    }

    @Override
    public Integer taskUpdate(Task newTask) {
        Task t = tasks.get(newTask.getId());
        if (t == null) {
            return null;
        }
        tasks.put(newTask.getId(), newTask);
        return newTask.getId();
    }

    @Override
    public void deleteTaskById(Integer id) {
        tasks.remove(id);
    }


    /// Для model.Epic

    @Override
    public ArrayList<Epic> getEpics() {
        if (epics.isEmpty()) {
            return null;
        }
        return new ArrayList<>(epics.values());
    }

    @Override
    public void removeAllEpics() {
        for (Epic epic : epics.values()) {
            Integer epicId = epic.getId();
            ArrayList<Integer> subtakIds = epics.get(epicId).getSubtakIds();
            for (Integer subtakId : subtakIds) {
                subtasks.remove(subtakId);
            }
        }

        epics.clear();
    }

    @Override
    public Epic getEpicById(Integer id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            return null;
        }
        historyManagers.add(epic);
        return epics.get(id);
    }

    @Override
    public Integer createEpic(Epic newEpic) {
        Integer id = ++ids;
        newEpic.setId(id);
        epics.put(id, newEpic);
        return newEpic.getId();
    }

    @Override
    public Integer epicUpdate(Epic newEpic) {
        Epic epic = epics.get(newEpic.getId());
        if (epic == null) {
            return null;
        }
        epics.put(newEpic.getId(), newEpic);
        return newEpic.getId();
    }

    @Override
    public void deleteEpicById(Integer id) {
        Epic epic = epics.get(id);
        ArrayList<Integer> subtakIds = epic.getSubtakIds();
        for (Integer subtakId : subtakIds) {
            subtasks.remove(subtakId);
        }
        epics.remove(id);
    }

    /// Для model.SubTask

    @Override
    public ArrayList<SubTask> getSubTasks() {
        if (subtasks.isEmpty()) {
            return null;
        }
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void removeAllSubTasks() {
        for (Epic epic : epics.values()) {
            epic.getSubtakIds().clear();
            checkEpicStatus(epic.getId());
        }
        subtasks.clear();
    }

    @Override
    public SubTask getSubTaskById(Integer id) {
        SubTask s = subtasks.get(id);
        if (s == null) {
            return null;
        }
        historyManagers.add(s);
        return subtasks.get(id);
    }

    @Override
    public Integer createSubTask(SubTask newSubtask) {
        Integer id = ++ids;
        newSubtask.setId(id);
        subtasks.put(id, newSubtask);
        Integer epicId = newSubtask.getEpicId();

        if (!epics.containsKey(epicId)) {
            return null;
        }

        ArrayList<Integer> subtakIds = epics.get(epicId).getSubtakIds();
        subtakIds.add(id);
        checkEpicStatus(epicId);
        return newSubtask.getId();
    }

    @Override
    public Integer subTaskUpdate(SubTask newSubtask) {
        subtasks.put(newSubtask.getId(), newSubtask);
        Integer epicId = subtasks.get(newSubtask.getId()).getEpicId();
        if (!epics.containsKey(epicId)) {
            return null;
        }
        checkEpicStatus(epicId);
        return newSubtask.getId();
    }

    @Override
    public void deleteSubTaskById(Integer id) {
        Integer epicId = subtasks.get(id).getEpicId();
        ArrayList<Integer> subtakIds = epics.get(epicId).getSubtakIds();
        subtakIds.remove(id);
        subtasks.remove(id);
        checkEpicStatus(epicId);
    }

    /// Дополнительные методы:

    @Override
    public ArrayList<SubTask> getSubTasksByEpicId(Integer epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return null;
        }

        ArrayList<Integer> subtakIds = epic.getSubtakIds();
        ArrayList<SubTask> subtask = new ArrayList<>();
        for (Integer subtakId : subtakIds) {
            subtask.add(subtasks.get(subtakId));
        }

        return subtask;
    }

    private void checkEpicStatus(Integer epicId) {
        Epic epic = epics.get(epicId);

        int countNew = 0;
        int countDone = 0;

        for (Integer subtaskId : epic.getSubtakIds()) {
            SubTask subtask = subtasks.get(subtaskId);
            if (subtask.getTaskStatus() == TaskStatus.NEW) {
                countNew++;
            }
            if (subtask.getTaskStatus() == TaskStatus.DONE) {
                countDone++;
            }
        }

        if (epic.getSubtakIds().isEmpty() || countNew == epic.getSubtakIds().size()) {
            epic.setTaskStatus(TaskStatus.NEW);
        } else if (countDone == epic.getSubtakIds().size()) {
            epic.setTaskStatus(TaskStatus.DONE);
        } else {
            epic.setTaskStatus(TaskStatus.IN_PROGRESS);
        }
    }
    ///  История последних просмотренных задач
    @Override
    public ArrayList<Task> getHistory(){
        return historyManagers.getHistory();
    }
}
