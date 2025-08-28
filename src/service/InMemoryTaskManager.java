package service;

import model.Epic;
import model.SubTask;
import model.Task;
import status.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class InMemoryTaskManager implements TaskManager {

    private Integer ids = 0;

    protected final HashMap<Integer, Task> tasks = new HashMap<>();

    protected final HashMap<Integer, Epic> epics = new HashMap<>();

    protected final HashMap<Integer, SubTask> subtasks = new HashMap<>();

    private final TreeSet<Task> prioritizedTasks = new TreeSet<>();

    private final HistoryManager historyManagers = Managers.getDefaultHistory();


    /// Для model.Task
    @Override
    public List<Task> getTasks() {
        return tasks.values().stream().toList();
    }

    @Override
    public void removeAllTasks() {
        if (tasks.isEmpty()) {
            return;
        } else {
            for (Integer taskId : tasks.keySet()) {
                historyManagers.remove(taskId);
            }
        }
        tasks.clear();
        updatePrioritizedTasks();
    }

    @Override
    public Task getTaskById(Integer id) {
        Task t = tasks.get(id);
        if (t == null) {
            return null;
        }
        historyManagers.add(t);
        updatePrioritizedTasks();
        return tasks.get(id);
    }

    @Override
    public Integer createTask(Task newTask) {
        int id = ++ids;
        newTask.setId(id);
        tasks.put(id, newTask);
        updatePrioritizedTasks();
        return id;
    }

    @Override
    public Integer taskUpdate(Task newTask) {
        Task t = tasks.get(newTask.getId());
        if (t == null) {
            return null;
        }
        tasks.put(newTask.getId(), newTask);
        updatePrioritizedTasks();
        return newTask.getId();
    }

    @Override
    public void deleteTaskById(Integer id) {
        tasks.remove(id);
        historyManagers.remove(id);
        updatePrioritizedTasks();
    }


    /// Для model.Epic

    @Override
    public List<Epic> getEpics() {
        return epics.values().stream().toList();
    }

    @Override
    public void removeAllEpics() {
        for (Integer subtakId : subtasks.keySet()) {
            historyManagers.remove(subtakId);
        }

        for (Integer epicId : epics.keySet()) {
            historyManagers.remove(epicId);
        }
        subtasks.clear();
        epics.clear();
        updatePrioritizedTasks();
    }

    @Override
    public Epic getEpicById(Integer id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            return null;
        }
        historyManagers.add(epic);
        updatePrioritizedTasks();
        return epics.get(id);
    }

    @Override
    public Integer createEpic(Epic newEpic) {
        Integer id = ++ids;
        newEpic.setId(id);
        epics.put(id, newEpic);
        updatePrioritizedTasks();
        return newEpic.getId();
    }

    @Override
    public Integer epicUpdate(Epic newEpic) {
        Epic epic = epics.get(newEpic.getId());
        if (epic == null) {
            return null;
        }
        epics.put(newEpic.getId(), newEpic);
        updatePrioritizedTasks();
        return newEpic.getId();
    }

    @Override
    public void deleteEpicById(Integer id) {
        Epic epic = epics.get(id);
        ArrayList<Integer> subtakIds = epic.getSubtakIds();
        for (Integer subtakId : subtakIds) {
            historyManagers.remove(subtakId);
            subtasks.remove(subtakId);
        }

        historyManagers.remove(id);
        epics.remove(id);
        updatePrioritizedTasks();
    }

    /// Для model.SubTask

    @Override
    public List<SubTask> getSubTasks() {
        return subtasks.values().stream().toList();
    }

    @Override
    public void removeAllSubTasks() {

        epics.values().forEach(epic -> {
            epic.getSubtakIds().clear();
            checkEpicStatus(epic.getId());
        });

        if (subtasks.isEmpty()) {
            return;
        } else {
            subtasks.keySet().forEach(epic -> historyManagers.remove(epic));
        }
        subtasks.clear();
        updatePrioritizedTasks();
    }

    @Override
    public SubTask getSubTaskById(Integer id) {
        SubTask s = subtasks.get(id);
        if (s == null) {
            return null;
        }
        historyManagers.add(s);
        updatePrioritizedTasks();
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

        Epic epic = epics.get(epicId);

        if (epic.getStartTime() == null || epic.getStartTime().isAfter(newSubtask.getStartTime())) {
            epic.setStartTime(newSubtask.getStartTime());
        }

        if (epic.getEndTime() == null || epic.getEndTime().isBefore(newSubtask.getEndTime())) {
            epic.setEndTime(newSubtask.getEndTime());
        }

        epic.setDuration(epic.getDuration().plus(newSubtask.getDuration()));

        checkEpicStatus(epicId);
        updatePrioritizedTasks();
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
        updatePrioritizedTasks();
        return newSubtask.getId();
    }

    @Override
    public void deleteSubTaskById(Integer id) {
        Integer epicId = subtasks.get(id).getEpicId();
        ArrayList<Integer> subtakIds = epics.get(epicId).getSubtakIds();
        subtakIds.remove(id);
        historyManagers.remove(id);
        subtasks.remove(id);
        checkEpicStatus(epicId);
        updatePrioritizedTasks();
    }

    /// Дополнительные методы:

    @Override
    public List<SubTask> getSubTasksByEpicId(Integer epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return null;
        }

        List<Integer> subtakIds = epic.getSubtakIds();
        List<SubTask> subtask = subtakIds.stream()
                .map(subtasks::get)
                .toList();


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
    public ArrayList<Task> getHistory() {
        return historyManagers.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return prioritizedTasks.stream().toList();
    }

    @Override
    public boolean isIntersectedTasks(Task t1, Task t2) {
        return ((t1.getStartTime().isBefore(t2.getEndTime()))) && (t2.getStartTime().isBefore(t1.getEndTime()));
    }

    private void updatePrioritizedTasks() {
        prioritizedTasks.clear();
        for (Task task : tasks.values()) {
            if (task.getStartTime() == null) {
                continue;
            }
            prioritizedTasks.add(task);
        }

        for (Epic epic : epics.values()) {
            if (epic.getStartTime() == null) {
                continue;
            }
            prioritizedTasks.add(epic);
        }

        for (SubTask subtask : subtasks.values()) {
            if (subtask.getStartTime() == null) {
                continue;
            }
            prioritizedTasks.add(subtask);
        }
    }

    private boolean isIntersectsExistingTask(Task input) {
        List<Task> tasks = prioritizedTasks.stream()
                .filter((task -> isIntersectedTasks(task, input)))
                .toList();
        return !tasks.isEmpty();
    }
}
