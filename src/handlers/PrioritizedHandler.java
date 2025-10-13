package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import service.TaskManager;

import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler {


    public PrioritizedHandler(TaskManager manager, Gson gson) {
        super(manager, gson);
    }

    public void handle(HttpExchange t) throws IOException {

        if (RequestMethod.valueOf(t.getRequestMethod()).equals(RequestMethod.GET)) {
            String respounse = gson.toJson(managers.getPrioritizedTasks());
            sendText(t, respounse, 200);
        } else {
            sendNotFound(t, "Данный метод не поддерживается", 404);
        }
    }
}
