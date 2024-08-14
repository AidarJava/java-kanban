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
                    checkAndResponseObjectFromManager("Task", idFromGet, taskManager, httpExchange);
                }
                break;

            case "POST":
                Integer idFromPost = getIdFromPath(httpExchange.getRequestURI().getPath());
                String responsePost = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                if (idFromPost == null) {
                    Task newTask = HttpTaskServer.getGson().fromJson(responsePost, Task.class);
                    if (!taskManager.checkToaddNoIntersectTask(newTask)) { //проверка на пересечение по времени
                        sendHasInteractions(httpExchange, "Время выполнение задачи пересекается с существующими!");
                        break;
                    }
                    taskManager.createTask(newTask);
                    if (taskContainsInManager(newTask.getId(), taskManager)) { //проверка на успешное добавление
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
                    if (taskContainsInManager(idFromDelete, taskManager)) {
                        Task task = taskManager.getTaskById(idFromDelete);
                        taskManager.deleteTaskById(idFromDelete);
                        if (!taskContainsInManager(task.getId(), taskManager)) {
                            sendText(httpExchange, "Задача успешно удалена.", 200);
                            break;
                        }
                    } else {
                        sendNotFound(httpExchange, "Такой задачи нет в списке!");
                    }
                }
                break;
            default:
                throw new IOException();
        }
    }
}
