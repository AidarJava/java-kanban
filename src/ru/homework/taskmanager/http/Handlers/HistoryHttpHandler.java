package ru.homework.taskmanager.http.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.homework.taskmanager.http.HttpTaskServer;
import ru.homework.taskmanager.service.TaskManager;

import java.io.IOException;

public class HistoryHttpHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public HistoryHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        if (httpExchange.getRequestMethod().equals("GET")) {
            String responseGet = HttpTaskServer.getGson().toJson(taskManager.getHistoryManager().getHistory());
            sendText(httpExchange, responseGet, 200);
        } else throw new IOException();

    }
}