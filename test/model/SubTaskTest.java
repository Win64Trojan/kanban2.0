package model;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static java.time.Month.OCTOBER;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SubTaskTest {

    @Test
    void IdEqualsSubtaskTest() {
        SubTask subTask = new SubTask("SubTask", "SubTask", LocalDateTime.of(2025, OCTOBER, 10, 21, 0), Duration.ofMinutes(5), 6);
        SubTask subTask1 = new SubTask("SubTask", "SubTask", LocalDateTime.of(2025, OCTOBER, 10, 21, 0), Duration.ofMinutes(5), 6);

        assertEquals(subTask.getEpicId(), subTask1.getEpicId());
    }
}