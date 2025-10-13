package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import model.Epic;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class EpicsHandler extends BaseHttpHandler {
    public EpicsHandler(TaskManager manager, Gson gson) {
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
        if (path.equals("/epics")) {
            handleGetList(t);
        } else if (pathSplit.length == 3) {
            handleGetById(t);
        } else if (pathSplit.length == 4 && pathSplit[3].equals("subtasks")) {
            handleGetEpicSubtask(t);
        }
    }

    private void handleGetList(HttpExchange t) throws IOException {
        String responseList = gson.toJson(managers.getEpics());
        sendText(t, responseList, 200);
    }

    private void handleGetById(HttpExchange t) throws IOException {

        int pathId = Integer.parseInt(t.getRequestURI().getPath().split("/")[2]);
        Epic epic = managers.getEpicById(pathId);
        if (epic == null) {
            sendNotFound(t, "Epic not found", 404);
        } else {
            String response = gson.toJson(epic);
            sendText(t, response, 200);
        }
    }

    private void handleGetEpicSubtask(HttpExchange t) throws IOException {
        int pathId = Integer.parseInt(t.getRequestURI().getPath().split("/")[2]);
        String response = gson.toJson(managers.getSubTasksByEpicId(pathId));
        sendText(t, response, 200);
    }

    private void handePost(HttpExchange t) throws IOException {
        try {

            String updateTask = new String(t.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Epic epic = gson.fromJson(updateTask, Epic.class);

            if (epic.getId() == null) {
                if (managers.isIntersectsExistingTask(epic)) {
                    throw new Exception("Задача не может быть добавлена, так как пересекается с текущей");
                }
                managers.createEpic(epic);
                sendText(t, "Epic created", 201);
            } else {

                if (managers.getEpicById(epic.getId()) == null) {
                    throw new Exception("Задачи с таким айди не существует, проверте правильность введенного айдти");
                } else if (managers.isIntersectsExistingTask(epic)) {
                    sendHasInteractions(t);
                }

                managers.epicUpdate(epic);
                sendText(t, "Epic updated", 201);
            }
        } catch (Exception e) {
            sendNotFound(t, e.getMessage(), 406);
        }
    }

    private void handleDelete(HttpExchange t) throws IOException {

        int pathId = Integer.parseInt(t.getRequestURI().getPath().split("/")[2]);
        if (managers.getEpicById(pathId) == null) {
            sendNotFound(t, "Epic not found", 404);
        }
        managers.deleteEpicById(pathId);
        sendText(t, "Epic deleted", 200);
    }
}
