package model;

import status.TaskStatus;

public class SubTask extends Task {

    private Integer epicId;

    public SubTask(String taskName, String taskDesc, Integer epicId) {
        super(taskName, taskDesc);
        this.epicId = epicId;
    }

    public SubTask(String taskName, String taskDesc, Integer id, TaskStatus taskStatus, Integer epicId) {
        super(taskName, taskDesc, id, taskStatus);
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
        return "SubTask{ " +
                "id SubTask= " + id +
                ", epicId=" + epicId +
                ", SubTaskName='" + taskName + '\'' +
                ", SubTaskDesc='" + taskDesc + '\'' +
                ", SubTaskStatus=" + taskStatus +
                '}';
    }
}
