import com.google.gson.Gson;
import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HttpTaskServerTest {


    TaskManager taskManager = Managers.getDefault();
    HttpTaskServer server = new HttpTaskServer(8080, taskManager);
    Gson gson = server.getGson();


    @BeforeEach
    void setUp() {
        server.start();
        taskManager.removeAllTasks();
        taskManager.removeAllSubTasks();
        taskManager.removeAllEpics();


    }

    @AfterEach
    void stopServer() {
        server.stop();
    }

    @Test
    void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2", LocalDateTime.now(), Duration.ofMinutes(5));

        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());

        List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются");
        assertEquals(1, tasks.size(), "Неккоректое количество задач");
        assertEquals("Test 2", tasks.get(0).getTaskName(), "Некорректное имя задачи");
    }

    @Test
    void testAddEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic 1", "Testing epic 1");

        String epicJson = gson.toJson(epic);


        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());

        List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Задачи не возвращаются");
        assertEquals(1, epics.size(), "Неккоректое количество задач");
        assertEquals("Epic 1", epics.get(0).getTaskName(), "Некорректное имя задачи");
    }

    @Test
    void testAddSubtask() throws IOException, InterruptedException {
        SubTask subTask = new SubTask("Subtask 1", "Test subtask 1", LocalDateTime.now(), Duration.ofMinutes(5), 1);
        String subTaskJson = gson.toJson(subTask);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(subTaskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());

        List<SubTask> subTasks = taskManager.getSubTasks();

        assertNotNull(subTasks, "Задачи не возвращаются");
        assertEquals(1, subTasks.size(), "Неккоректое количество задач");
        assertEquals("Subtask 1", subTasks.get(0).getTaskName(), "Некорректное имя задачи");
    }

    @Test
    void testGetTaskById() throws IOException, InterruptedException {
        // Сначала создаем задачу
        Task task = new Task("Get Test", "Get Description", LocalDateTime.now(), Duration.ofMinutes(30));
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI createUri = URI.create("http://localhost:8080/tasks");
        HttpRequest createRequest = HttpRequest.newBuilder()
                .uri(createUri)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> createResponse = client.send(createRequest, HttpResponse.BodyHandlers.ofString());

        // Теперь получаем задачу по ID
        URI getUri = URI.create("http://localhost:8080/tasks/2");
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(getUri)
                .build();

        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, getResponse.statusCode());
        Task retrievedTask = gson.fromJson(getResponse.body(), Task.class);
        assertEquals("Get Test", retrievedTask.getTaskName());
    }

    @Test
    void testUpdateTask() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2", LocalDateTime.now(), Duration.ofMinutes(5));

        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        URI getUri = URI.create("http://localhost:8080/tasks/2");
        HttpRequest getTask = HttpRequest.newBuilder().uri(getUri).GET().build();
        HttpResponse<String> getTaskResponse = client.send(getTask, HttpResponse.BodyHandlers.ofString());
        Task getTaskHttp = gson.fromJson(getTaskResponse.body(), Task.class);
        getTaskHttp.setTaskName("Updated Task");

        String updateTaskJson = gson.toJson(getTaskHttp);
        HttpRequest request2 = HttpRequest.newBuilder().uri(getUri).POST(HttpRequest.BodyPublishers.ofString(updateTaskJson)).build();
        HttpResponse<String> updateTaskResponse = client.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, updateTaskResponse.statusCode());
        assertEquals("Task updated", updateTaskResponse.body());

        URI getUriUpdate = URI.create("http://localhost:8080/tasks/2");
        HttpRequest getTaskUpdate = HttpRequest.newBuilder().uri(getUriUpdate).GET().build();
        HttpResponse<String> getTaskResponseUpdate = client.send(getTaskUpdate, HttpResponse.BodyHandlers.ofString());
        Task getTaskHttpUpdate = gson.fromJson(getTaskResponseUpdate.body(), Task.class);

        assertEquals("Updated Task", getTaskHttpUpdate.getTaskName());

    }
}