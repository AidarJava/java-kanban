package ru.homework.taskmanager.http.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.homework.taskmanager.http.HttpTaskServer;
import ru.homework.taskmanager.model.Epic;
import ru.homework.taskmanager.model.Subtask;
import ru.homework.taskmanager.service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class EpicsHttpHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public EpicsHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        switch (httpExchange.getRequestMethod()) {

            case "GET":
                Integer idFromGet = getIdFromPath(httpExchange.getRequestURI().getPath());
                String stringFromGet = getStringFromPath(httpExchange.getRequestURI().getPath());

                if (idFromGet != null) { //проверяем путь на наличие id
                    if (epicContainsInManager(idFromGet, taskManager)) {
                        Epic epic = (Epic) taskManager.getEpicById(idFromGet);
                        if ("subtasks".equals(stringFromGet)) { //проверяем путь на запрос подзадач
                            List<Subtask> sub = epic.getSubtasks().stream()
                                    .map(inta -> (Subtask) taskManager.getSubtaskById(inta))
                                    .collect(Collectors.toList());
                            String responseGet = HttpTaskServer.getGson().toJson(sub);
                            sendText(httpExchange, responseGet, 200);
                            break;
                        } else {
                            String responseGet = HttpTaskServer.getGson().toJson(epic);
                            sendText(httpExchange, responseGet, 200);
                        }

                    } else {
                        sendNotFound(httpExchange, "Такого эпика нет в списке!");
                    }
                } else {
                    List<Epic> epics = taskManager.getEpics();
                    String responseGet = HttpTaskServer.getGson().toJson(epics);
                    sendText(httpExchange, responseGet, 200);
                }
                break;

            case "POST":
                Integer idFromPost = getIdFromPath(httpExchange.getRequestURI().getPath());
                String responsePost = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                if (idFromPost == null) {
                    Epic newEpic = HttpTaskServer.getGson().fromJson(responsePost, Epic.class);
                    taskManager.createEpic(newEpic);
                    if (epicContainsInManager(newEpic.getId(), taskManager)) { //проверка на успешное добавление
                        sendText(httpExchange, responsePost, 201);
                        break;
                    }
                } else {
                    Epic updateEpic = HttpTaskServer.getGson().fromJson(responsePost, Epic.class);
                    taskManager.updateEpic(updateEpic);
                    sendText(httpExchange, responsePost, 201);
                }
                break;
            case "DELETE":
                Integer idFromDelete = getIdFromPath(httpExchange.getRequestURI().getPath());
                if (idFromDelete != null) {
                    if (epicContainsInManager(idFromDelete, taskManager)) {
                        Epic epic = (Epic) taskManager.getEpicById(idFromDelete);
                        taskManager.deleteEpicById(idFromDelete);
                        if (!epicContainsInManager(epic.getId(), taskManager)) {
                            sendText(httpExchange, "Эпик успешно удален.", 200);
                            break;
                        }
                    } else {
                        sendNotFound(httpExchange, "Такого эпика нет в списке!");
                    }
                }
                break;
            default:
                throw new IOException();
        }
    }
}