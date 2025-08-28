package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Epic extends Task {

    private LocalDateTime endTime;

    protected ArrayList<Integer> subtakIds = new ArrayList<>();

    public Epic(String taskName, String taskDesc) {
        super(taskName, taskDesc, null, Duration.ofMinutes(0));
    }


    public ArrayList<Integer> getSubtakIds() {
        return subtakIds;
    }

    public void addSubtaksId(Integer subtaskId) {
        subtakIds.add(subtaskId);
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {

        String startTimeString = "";
        String endTimeString = "";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

        if (startTime != null) {
            startTimeString = startTime.format(formatter);
        }

        if (endTimeString != null) {
            endTimeString = endTime.format(formatter);
        }

        return "Epic{" +
                "id Epic=" + id +
                ", subtaksId=" + subtakIds.size() +
                ", epicName='" + taskName + '\'' +
                ", epicDesc='" + taskDesc + '\'' +
                ", epicStatus=" + taskStatus +
                ", start_time=" + startTimeString +
                ", duration=" + duration.toMinutes() + "мин." +
                ", end_time=" + endTimeString +
                '}';
    }
}
