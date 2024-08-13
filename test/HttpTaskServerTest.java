import com.google.gson.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import ru.homework.taskmanager.enums.TaskStatus;
import ru.homework.taskmanager.http.HttpTaskServer;
import ru.homework.taskmanager.model.Epic;
import ru.homework.taskmanager.model.Subtask;
import ru.homework.taskmanager.model.Task;
import ru.homework.taskmanager.service.Managers;
import ru.homework.taskmanager.service.TaskManager;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {

    TaskManager manager = Managers.getDefault();
    HttpTaskServer taskServer = new HttpTaskServer(manager);

    public HttpTaskServerTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.deleteAllTasks();
        manager.deleteAllSubtasks();
        manager.deleteAllEpics();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        LocalDateTime time1 = LocalDateTime.of(2075, 1, 1, 1, 1);
        Task task = new Task("Task1", "Checking create1", TaskStatus.NEW, Duration.ofMinutes(5), time1);
        String responseGet = HttpTaskServer.getGson().toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(responseGet))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> tasksFromManager = manager.getTasks();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals(201, response.statusCode(), "Статусы ответа не совпадают.");
        assertEquals(responseGet, response.body(), "Задачи не совпадают.");
        assertEquals("Task1", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testCreateSubtask() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Epic1", "Checking create1");
        manager.createEpic(epic1);
        LocalDateTime time2 = LocalDateTime.of(2024, 2, 3, 3, 44);
        Subtask sub1 = new Subtask("Subtask1", "Checking create1", TaskStatus.IN_PROGRESS, epic1.getId(), Duration.ofMinutes(6), time2);
        manager.createSubtask(sub1);
        LocalDateTime time3 = LocalDateTime.of(2024, 2, 3, 3, 30);
        Subtask sub2 = new Subtask("Subtask2", "Checking create2", TaskStatus.NEW, epic1.getId(), Duration.ofMinutes(3), time3);
        String responseGet = HttpTaskServer.getGson().toJson(sub2);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .POST(HttpRequest.BodyPublishers.ofString(responseGet))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Subtask> subtasksFromManager = manager.getSubtascs();
        assertEquals(2, subtasksFromManager.size(), "Некорректное количество задач");
        assertEquals(201, response.statusCode(), "Статусы ответа не совпадают.");
        assertEquals(responseGet, response.body(), "Подзадачи не совпадают.");
    }

    @Test
    public void testGetEpic() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Epic1", "Checking create1");
        manager.createEpic(epic1);
        LocalDateTime time2 = LocalDateTime.of(2024, 2, 3, 3, 44);
        Subtask sub1 = new Subtask("Subtask1", "Checking create1", TaskStatus.IN_PROGRESS, epic1.getId(), Duration.ofMinutes(6), time2);
        manager.createSubtask(sub1);
        LocalDateTime time3 = LocalDateTime.of(2024, 2, 3, 3, 30);
        Subtask sub2 = new Subtask("Subtask2", "Checking create2", TaskStatus.NEW, epic1.getId(), Duration.ofMinutes(3), time3);
        manager.createSubtask(sub2);
        String responseGet = HttpTaskServer.getGson().toJson(epic1);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/epics" + epic1.getId()))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonArray jsonArray = JsonParser.parseString(response.body()).getAsJsonArray();
        JsonElement firstElement = jsonArray.get(0);
        JsonObject actualJsonObject = firstElement.getAsJsonObject();
        JsonObject expectedJsonObject = JsonParser.parseString(responseGet).getAsJsonObject();
        assertEquals(200, response.statusCode(), "Статусы ответа не совпадают.");
        assertEquals(expectedJsonObject, actualJsonObject, "Эпики не совпадают.");
    }

}
