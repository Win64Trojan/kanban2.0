package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import model.Task;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TasksHandler extends BaseHttpHandler {
    public TasksHandler(TaskManager manager, Gson gson) {
        super(manager, gson);
    }

    @Override
    public void handle(HttpExchange t) throws IOException {

        switch (RequestMethod.valueOf(t.getRequestMethod())) {
            case GET:
                handleGet(t);
                break;
            case POST:
                handePost(t);
                break;
            case DELETE:
                handleDelete(t);
                break;
            default:
                sendNotFound(t, "Такой метод не предусмотрен", 400);

        }
    }

    private void handleGet(HttpExchange t) throws IOException {
        String path = t.getRequestURI().getPath();
        if (path.equals("/tasks")) {
            handleGetList(t);
        } else {
            handleGetById(t);
        }
    }

    private void handleGetList(HttpExchange t) throws IOException {
        String responseList = gson.toJson(managers.getTasks());
        sendText(t, responseList, 200);
    }

    private void handleGetById(HttpExchange t) throws IOException {

        int pathId = Integer.parseInt(t.getRequestURI().getPath().split("/")[2]);
        Task task = managers.getTaskById(pathId);
        if (task == null) {
            sendNotFound(t, "Task not found", 404);
        } else {
            String response = gson.toJson(task);
            sendText(t, response, 200);
        }
    }

    private void handePost(HttpExchange t) throws IOException {
        try {

            String updateTask = new String(t.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Task task = gson.fromJson(updateTask, Task.class);

            if (task.getId() == null) {
                if (managers.isIntersectsExistingTask(task)) {
                    throw new Exception("Задача не может быть добавлена, так как пересекается с текущей");
                }
                managers.createTask(task);
                sendText(t, "Task created", 201);
            } else {

                if (managers.getTaskById(task.getId()) == null) {
                    throw new Exception("Задачи с таким айди не существует, проверте правильность введенного айдти");
                }

                managers.taskUpdate(task);
                sendText(t, "Task updated", 201);
            }
        } catch (Exception e) {
            sendNotFound(t, e.getMessage(), 406);
        }
    }

    private void handleDelete(HttpExchange t) throws IOException {

        int pathId = Integer.parseInt(t.getRequestURI().getPath().split("/")[2]);
        managers.deleteTaskById(pathId);
        sendText(t, "Task deleted", 200);
    }

}