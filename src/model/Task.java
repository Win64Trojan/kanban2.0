package model;

import status.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task implements Comparable<Task> {

    protected String taskName;

    protected String taskDesc;

    protected Integer id;

    protected TaskStatus taskStatus = TaskStatus.NEW;

    protected LocalDateTime startTime;

    protected Duration duration;

    public Task(String taskName, String taskDesc, LocalDateTime startTime, Duration duration) {
        this.taskName = taskName;
        this.taskDesc = taskDesc;
        this.startTime = startTime;
        this.duration = duration;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    @Override
    public int compareTo(Task o) {
        if (startTime == null || startTime.isAfter(o.getStartTime())) {
            return 1;
        }
        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(taskName, task.taskName) && Objects.equals(taskDesc, task.taskDesc) && Objects.equals(id, task.id) && taskStatus == task.taskStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskName, taskDesc, id, taskStatus);
    }

    @Override
    public String toString() {

        String startTimeString = "";
        if (startTime != null) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
            startTimeString = startTime.format(dateTimeFormatter);
        }

        return "Tasks.model.Task{" +
                "id=" + id +
                ", taskName='" + taskName + '\'' +
                ", taskDesc='" + taskDesc + '\'' +
                ", taskStatus=" + taskStatus +
                ", start_time=" + startTimeString +
                ", duration=" + duration.toMinutes() + "мин." +
                '}';
    }
}
