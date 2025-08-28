package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SubTask extends Task {

    private Integer epicId;

    public SubTask(String taskName, String taskDesc, LocalDateTime startTime, Duration duration, Integer epicId) {
        super(taskName, taskDesc, startTime, duration);
        this.epicId = epicId;
    }


    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        String startTimeString = "";
        if (startTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
            startTimeString = startTime.format(formatter);
        }

        return "SubTask{ " +
                "id SubTask= " + id +
                ", epicId=" + epicId +
                ", SubTaskName='" + taskName + '\'' +
                ", SubTaskDesc='" + taskDesc + '\'' +
                ", SubTaskStatus=" + taskStatus +
                ", start_time=" + startTimeString +
                ", duration=" + duration.toMinutes() + "мин." +
                '}';
    }
}
