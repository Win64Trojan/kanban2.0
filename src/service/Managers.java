package service;

import java.io.File;

public class Managers {

    static final File fileSave = new File("saveTasks.csv");

    private Managers() {
    }

    public static TaskManager getDefault() {
        if (fileSave.exists()) {
            return FileBackedTaskManager.loadFromFile(fileSave);
        }
        return new FileBackedTaskManager(fileSave);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
