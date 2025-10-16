package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import model.SubTask;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SubtasksHandler extends BaseHttpHandler {
    public SubtasksHandler(TaskManager manager, Gson gson) {
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
                sendText(t, "Такой метод не предусмотрен", 400);

        }
    }

    private void handleGet(HttpExchange t) throws IOException {
        String path = t.getRequestURI().getPath();
        String[] pathSplit = path.split("/");
        if (path.equals("/subtasks")) {
            handleGetList(t);
        } else if (pathSplit.length == 3) {
            handleGetById(t);
        }
    }

    private void handleGetList(HttpExchange t) throws IOException {
        String responseList = gson.toJson(managers.getSubTasks());
        sendText(t, responseList, 200);
    }

    private void handleGetById(HttpExchange t) throws IOException {

        int pathId = Integer.parseInt(t.getRequestURI().getPath().split("/")[2]);
        SubTask subTask = managers.getSubTaskById(pathId);
        if (subTask == null) {
            sendText(t, "SubTask not found", 404);
        } else {
            String response = gson.toJson(subTask);
            sendText(t, response, 200);
        }
    }

    private void handePost(HttpExchange t) throws IOException {
        try {
            if (t.getRequestURI().getPath().equals("/subtasks")) {

                String updateTask = new String(t.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                SubTask subTask = gson.fromJson(updateTask, SubTask.class);

                if (subTask.getId() == null) {
                    if (managers.isIntersectsExistingTask(subTask)) {
                        throw new Exception("Задача не может быть добавлена, так как пересекается с текущей");
                    }
                    managers.createSubTask(subTask);
                    sendText(t, "SubTask created", 201);
                } else {

                    if (managers.getSubTaskById(subTask.getId()) == null) {
                        throw new Exception("Задачи с таким айди не существует, проверте правильность введенного айдти");
                    } else if (managers.isIntersectsExistingTask(subTask)) {
                        sendHasInteractions(t);
                    }

                    managers.subTaskUpdate(subTask);
                    sendText(t, "SubTask updated", 201);
                }
            } else {
                sendNotFound(t, "id передается в формате json", 400);
            }
        } catch (Exception e) {
            sendNotFound(t, e.getMessage(), 406);
        }
    }

    private void handleDelete(HttpExchange t) throws IOException {

        int pathId = Integer.parseInt(t.getRequestURI().getPath().split("/")[2]);
        if (managers.getEpicById(pathId) == null) {
            sendNotFound(t, "SubTask not found", 404);
        }
        managers.deleteEpicById(pathId);
        sendText(t, "SubTask deleted", 200);
    }
}