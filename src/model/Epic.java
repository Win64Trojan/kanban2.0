package model;

import status.TaskStatus;

import java.util.ArrayList;

public class Epic extends Task {


    protected ArrayList<Integer> subtakIds = new ArrayList<>();

    public Epic(String taskName, String taskDesc) {
        super(taskName, taskDesc);
    }

    public Epic(String taskName, String taskDesc, Integer id, TaskStatus taskStatus) {
        super(taskName, taskDesc, id, taskStatus);
    }

    public ArrayList<Integer> getSubtakIds() {
        return subtakIds;
    }

    public void addSubtaksId(Integer subtaskId) {
        subtakIds.add(subtaskId);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtaksId=" + subtakIds +
                "epicName='" + taskName + '\'' +
                ", epicDesc='" + taskDesc + '\'' +
                ", id Epic=" + id +
                ", epicStatus=" + taskStatus +
                '}';
    }
}
