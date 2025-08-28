package model;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static java.time.Month.OCTOBER;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {

    @Test
    void IdEqualsTaskTest() {
        Task task = new Task("Task", "Task", LocalDateTime.of(2025, OCTOBER, 10, 21, 0), Duration.ofMinutes(5));
        Task task2 = new Task("Task", "Task", LocalDateTime.of(2025, OCTOBER, 10, 21, 0), Duration.ofMinutes(5));

        assertEquals(task, task2);
    }
}