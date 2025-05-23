package service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    TaskManager taskManager = new InMemoryTaskManager();
    HistoryManager historyManager = new InMemoryHistoryManager();

    @Test
    void getDefaultTest() {
        TaskManager taskManager2 = Managers.getDefault();
        assertEquals(taskManager.getClass(), taskManager2.getClass());
    }

    @Test
    void getDefaultHistoryTest() {
        HistoryManager historyManager2 = Managers.getDefaultHistory();
        assertEquals(historyManager.getClass(), historyManager2.getClass());
    }
}