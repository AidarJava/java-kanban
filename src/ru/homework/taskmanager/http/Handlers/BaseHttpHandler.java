package ru.homework.taskmanager.http.Handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.homework.taskmanager.http.HttpTaskServer;
import ru.homework.taskmanager.model.Epic;
import ru.homework.taskmanager.model.Subtask;
import ru.homework.taskmanager.service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {
    protected void sendText(HttpExchange h, String text, int statusCode) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(statusCode, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendHasInteractions(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(406, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendNotFound(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(404, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected Integer getIdFromPath(String path) {
        String[] arr = path.split("/");
        if (arr.length > 2) {
            return Integer.parseInt(arr[2]);
        }
        return null;
    }

    protected String getStringFromPath(String path) {
        String[] arr = path.split("/");
        if (arr.length > 3) {
            return arr[3];
        }
        return null;
    }

    protected boolean epicContainsInManager(Integer numId, TaskManager taskManager) {
        return taskManager.getEpics().contains((Epic) taskManager.getEpicById(numId));
    }

    protected boolean subtaskContainsInManager(Integer numId, TaskManager taskManager) {
        return taskManager.getSubtascs().contains((Subtask) taskManager.getSubtaskById(numId));
    }

    protected boolean taskContainsInManager(Integer numId, TaskManager taskManager) {
        return taskManager.getTasks().contains(taskManager.getTaskById(numId));
    }

    protected void checkAndResponseObjectFromManager(String string, Integer idFromGet, TaskManager taskManager, HttpExchange httpExchange) throws IOException {
        if ("Task".equals(string)) {
            if (taskContainsInManager(idFromGet, taskManager)) {
                String responseGet = HttpTaskServer.getGson().toJson(taskManager.getTaskById(idFromGet));
                sendText(httpExchange, responseGet, 200);
            } else {
                sendNotFound(httpExchange, "Такой задачи нет в списке!");
            }
        } else if ("Subtask".equals(string)) {
            if (subtaskContainsInManager(idFromGet, taskManager)) {
                String responseGet = HttpTaskServer.getGson().toJson(taskManager.getSubtaskById(idFromGet));
                sendText(httpExchange, responseGet, 200);
            } else {
                sendNotFound(httpExchange, "Такой подзадачи нет в списке!");
            }
        }

    }
}









