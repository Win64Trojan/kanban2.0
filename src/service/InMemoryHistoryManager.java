package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {

    private static final Integer HISTORY_SIZE = 10;

    private static ArrayList<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (history.size() == HISTORY_SIZE) {
            history.removeFirst();
            history.add(task);
        } else {
            history.add(task);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }

    public void removeHistory(){
        history.clear();
    }
}
