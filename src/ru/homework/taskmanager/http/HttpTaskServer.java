package ru.homework.taskmanager.http;

import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import ru.homework.taskmanager.http.Adapters.DurationTimeAdapter;
import ru.homework.taskmanager.http.Adapters.EpicAdapter;
import ru.homework.taskmanager.http.Adapters.LocalDateTimeAdapter;
import ru.homework.taskmanager.http.Handlers.*;
import ru.homework.taskmanager.model.Epic;
import ru.homework.taskmanager.service.FileBackedTaskManager;
import ru.homework.taskmanager.service.Managers;
import ru.homework.taskmanager.service.TaskManager;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

import com.google.gson.Gson;

import static ru.homework.taskmanager.service.FileBackedTaskManager.loadFromFile;

public class HttpTaskServer {
    private final HttpServer httpServer;
    private final TaskManager taskManager;
    public static final int PORT = 8080;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        this.taskManager = taskManager;
        this.httpServer.createContext("/tasks", new TasksHttpHandler(taskManager));
        this.httpServer.createContext("/epics", new EpicsHttpHandler(taskManager));
        this.httpServer.createContext("/subtasks", new SubtasksHttpHandler(taskManager));
        this.httpServer.createContext("/history", new HistoryHttpHandler(taskManager));
        this.httpServer.createContext("/prioritized", new PrioritizedHttpHandler(taskManager));
    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
    }

    public static void main(String[] args) throws IOException {
        String filePath = System.getProperty("user.dir") + "\\" + "manager.csv";
        File myfile = new File(filePath);
        TaskManager taskManager;
        if (myfile.exists() && myfile.isFile()) { //проверка на существование файла
            taskManager = loadFromFile(myfile); //читаем из файла
        } else {
            TaskManager manager = Managers.getDefault();
            taskManager = new FileBackedTaskManager(manager.getHistoryManager(), myfile);
        }
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
        httpTaskServer.stop();

//________шаблоны для тела запроса POST_______
//        {
//            "name": "Task1",
//                "description": "Checking create1",
//                "status": "NEW",
//                "duration": "PT5M",
//                "startTime": "01.01.2024, 01:01"
//        }
//___________________________________________________________
//        {
//            "name": "Epic1",
//                "description": "Checking create1"
//        }
//___________________________________________________________
//        {
//            "epicId": 2,
//                "name": "Sub2",
//                "description": "Checking create1",
//                "status": "NEW",
//                "duration": "PT5M",
//                "startTime": "01.01.2023, 01:01"
//        }
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationTimeAdapter())
                .registerTypeAdapter(Epic.class, new EpicAdapter())
                .create();
    }

}
