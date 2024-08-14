package ru.homework.taskmanager.http.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.homework.taskmanager.http.HttpTaskServer;
import ru.homework.taskmanager.model.Subtask;
import ru.homework.taskmanager.service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtasksHttpHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public SubtasksHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        switch (httpExchange.getRequestMethod()) {

            case "GET":
                Integer idFromGet = getIdFromPath(httpExchange.getRequestURI().getPath());
                if (idFromGet == null) {
                    List<Subtask> subtasks = taskManager.getSubtascs();
                    String responseGet = HttpTaskServer.getGson().toJson(subtasks);
                    sendText(httpExchange, responseGet, 200);
                } else {
                    checkAndResponseObjectFromManager("Subtask", idFromGet, taskManager, httpExchange);
                }
                break;

            case "POST":
                Integer idFromPost = getIdFromPath(httpExchange.getRequestURI().getPath());
                String responsePost = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                if (idFromPost == null) {
                    Subtask newSubtask = HttpTaskServer.getGson().fromJson(responsePost, Subtask.class);
                    if (!taskManager.checkToaddNoIntersectTask(newSubtask)) { //проверка на пересечение по времени
                        String str = "Время выполнение задачи пересекается с существующими!";
                        sendHasInteractions(httpExchange, str);
                        break;
                    }
                    taskManager.createSubtask(newSubtask);
                    if (subtaskContainsInManager(newSubtask.getId(), taskManager)) { //проверка на успешное добавление
                        sendText(httpExchange, responsePost, 201);
                        break;
                    }
                } else {
                    Subtask updateSubtask = HttpTaskServer.getGson().fromJson(responsePost, Subtask.class);
                    taskManager.updateSubtask(updateSubtask);
                    sendText(httpExchange, responsePost, 201);
                }
                break;
            case "DELETE":
                Integer idFromDelete = getIdFromPath(httpExchange.getRequestURI().getPath());
                if (idFromDelete != null) {
                    if (subtaskContainsInManager(idFromDelete, taskManager)) {
                        Subtask subtask = (Subtask) taskManager.getSubtaskById(idFromDelete);
                        taskManager.deleteSubtaskById(idFromDelete);
                        if (!subtaskContainsInManager(subtask.getId(), taskManager)) {
                            String responseGet = "Подзадача успешно удалена.";
                            sendText(httpExchange, responseGet, 200);
                            break;
                        }
                    } else {
                        String str = "Такой подзадачи нет в списке!";
                        sendNotFound(httpExchange, str);
                    }
                }
                break;
            default:
                throw new IOException();
        }
    }
}
