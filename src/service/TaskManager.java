package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private Integer ids = 0;

    private final HashMap<Integer, Task> tasks = new HashMap<>();

    private final HashMap<Integer, Epic> epics = new HashMap<>();

    private final HashMap<Integer, SubTask> subtasks = new HashMap<>();

    private ArrayList<Integer> subtakIds = new ArrayList<>();


    /// Для model.Task
    public ArrayList<Task> getTasks() {
        if (tasks.isEmpty()) {
            return null;
        }
        return new ArrayList<>(tasks.values());
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public Task getTaskById(Integer id) {
        Task t = tasks.get(id);
        if (t == null) {
            return null;
        }
        return tasks.get(id);
    }

    public Integer createTask(Task newTask) {
        int id = ++ids;
        newTask.setId(id);
        tasks.put(id, newTask);
        return id;
    }

    public Integer taskUpdate(Task newTask) {
        Task t = tasks.get(newTask.getId());
        if (t == null) {
            return null;
        }
        tasks.put(newTask.getId(), newTask);
        return newTask.getId();
    }

    public void deleteTaskById(Integer id) {
        tasks.remove(id);
    }


    /// Для model.Epic

    public ArrayList<Epic> getEpics() {
        if (epics.isEmpty()) {
            return null;
        }
        return new ArrayList<>(epics.values());
    }

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

    public Epic getEpicById(Integer id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            return null;
        }
        return epics.get(id);
    }

    public Integer createEpic(Epic newEpic) {
        Integer id = ++ids;
        newEpic.setId(id);
        epics.put(id, newEpic);
        return newEpic.getId();
    }

    public Integer epicUpdate(Epic newEpic) {
        Epic epic = epics.get(newEpic.getId());
        if (epic == null) {
            return null;
        }
        epics.put(newEpic.getId(), newEpic);
        return newEpic.getId();
    }

    public void deleteEpicById(Integer id) {
        Epic epic = epics.get(id);
        ArrayList<Integer> subtakIds = epic.getSubtakIds();
        for (Integer subtakId : subtakIds) {
            subtasks.remove(subtakId);
        }
        epics.remove(id);
    }

    /// Для model.SubTask

    public ArrayList<SubTask> getSubTasks() {
        if (subtasks.isEmpty()) {
            return null;
        }
        return new ArrayList<>(subtasks.values());
    }

    public void removeAllSubTasks() {
        for (Epic epic : epics.values()) {
            epic.getSubtakIds().clear();
        }
        subtasks.clear();
    }

    public SubTask getSubTaskById(Integer id) {
        return subtasks.get(id);
    }

    public Integer createSubTask(SubTask newSubtask) {
        Integer id = ++ids;
        newSubtask.setId(id);
        subtasks.put(id, newSubtask);
        Integer epicId = newSubtask.getEpicId();
        ArrayList<Integer> subtakIds = epics.get(epicId).getSubtakIds();
        subtakIds.add(id);
        return newSubtask.getId();
    }

    public Integer subTaskUpdate(SubTask newSubtask) {
        SubTask t = subtasks.get(newSubtask.getId());
        if (t == null) {
            return null;
        }
        subtasks.put(newSubtask.getId(), newSubtask);
        return newSubtask.getId();
    }

    public void deleteSubTaskById(Integer id) {
        Integer epicId = subtasks.get(id).getEpicId();
        ArrayList<Integer> subtakIds = epics.get(epicId).getSubtakIds();
        subtakIds.remove(id);
        subtasks.remove(id);
    }

    /// Дополнительные методы:

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

    }


//    Возможность хранить задачи всех типов. Для этого вам нужно выбрать подходящую коллекцию.
//    Методы для каждого из типа задач(Задача/Эпик/Подзадача):
//    a. Получение списка всех задач.
//    b. Удаление всех задач.
//    c. Получение по идентификатору.
//    d. Создание. Сам объект должен передаваться в качестве параметра.
//    e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
//    f. Удаление по идентификатору.
//    Дополнительные методы:
//    a. Получение списка всех подзадач определённого эпика.
//    Управление статусами осуществляется по следующему правилу:
//    a. Менеджер сам не выбирает статус для задачи. Информация о нём приходит менеджеру вместе с информацией о самой задаче.
//    По этим данным в одних случаях он будет сохранять статус, в других будет рассчитывать.
//    b. Для эпиков:
//    если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW.
//    если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE.
//    во всех остальных случаях статус должен быть IN_PROGRESS.
}
