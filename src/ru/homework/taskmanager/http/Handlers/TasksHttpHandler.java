package ru.homework.taskmanager.http.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.homework.taskmanager.http.HttpTaskServer;
import ru.homework.taskmanager.model.Task;
import ru.homework.taskmanager.service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TasksHttpHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public TasksHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        switch (httpExchange.getRequestMethod()) {

            case "GET":
                Integer idFromGet = getIdFromPath(httpExchange.getRequestURI().getPath());
                if (idFromGet == null) {
                    List<Task> tasks = taskManager.getTasks();
                    String responseGet = HttpTaskServer.getGson().toJson(tasks);
                    sendText(httpExchange, responseGet, 200);
                } else {
                    if (taskManager.getTasks().contains(taskManager.getTaskById(idFromGet))) {
                        Task task = taskManager.getTaskById(idFromGet);
                        String responseGet = HttpTaskServer.getGson().toJson(task);
                        sendText(httpExchange, responseGet, 200);
                    } else {
                        String str = "Такой задачи нет в списке!";
                        sendNotFound(httpExchange, str);
                    }
                }
                break;

            case "POST":
                Integer idFromPost = getIdFromPath(httpExchange.getRequestURI().getPath());
                String responsePost = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                if (idFromPost == null) {
                    Task newTask = HttpTaskServer.getGson().fromJson(responsePost, Task.class);
                    if (!taskManager.checkToaddNoIntersectTask(newTask)) { //проверка на пересечение по времени
                        String str = "Время выполнение задачи пересекается с существующими!";
                        sendHasInteractions(httpExchange, str);
                        break;
                    }
                    taskManager.createTask(newTask);
                    if (taskManager.getTasks().contains(newTask)) { //проверка на успешное добавление
                        sendText(httpExchange, responsePost, 201);
                        break;
                    }
                } else {
                    Task updateTask = HttpTaskServer.getGson().fromJson(responsePost, Task.class);
                    taskManager.updateTask(updateTask);
                    sendText(httpExchange, responsePost, 201);
                }
                break;
            case "DELETE":
                Integer idFromDelete = getIdFromPath(httpExchange.getRequestURI().getPath());
                if (idFromDelete != null) {
                    Task task = taskManager.getTaskById(idFromDelete);
                    taskManager.deleteTaskById(idFromDelete);
                    if (!taskManager.getTasks().contains(task)) {
                        String responseGet = "Задача успешно удалена.";
                        sendText(httpExchange, responseGet, 200);
                        break;
                    }
                }
                break;
            default:
                throw new IOException();
        }
    }
}
