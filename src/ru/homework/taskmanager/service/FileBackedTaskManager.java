package ru.homework.taskmanager.service;

import ru.homework.taskmanager.enums.TaskStatus;
import ru.homework.taskmanager.model.Epic;
import ru.homework.taskmanager.model.Subtask;
import ru.homework.taskmanager.model.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.io.FileWriter;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Objects;

public class FileBackedTaskManager extends InMemoryTaskManager {
    File myfile;

    public FileBackedTaskManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.myfile = file;
    }

    public static void main(String[] args) {
        String filePath = System.getProperty("user.dir") + "\\" + "manager.csv";
        File myfile = new File(filePath);
        TaskManager manager;
        if (myfile.exists() && myfile.isFile()) { //проверка на существование файла
            manager = loadFromFile(myfile); //читаем из файла
        } else {
            TaskManager taskManager = Managers.getDefault();
            manager = new FileBackedTaskManager(taskManager.getHistoryManager(), myfile);
        }
        System.out.println("Создаем задачи");
        LocalDateTime time1 = LocalDateTime.of(2024, 1, 1, 1, 1);
        Task task1 = new Task("Task1", "Checking create1", TaskStatus.NEW, Duration.ofMinutes(5), time1);
        System.out.println(manager.createTask(task1));
        LocalDateTime time11 = LocalDateTime.of(2023, 1, 10, 1, 33);
        Task task2 = new Task("Task2", "Checking create2", TaskStatus.NEW, Duration.ofMinutes(5), time11);
        System.out.println(manager.createTask(task2));
        System.out.println("Создаем эпик");
        Epic epic1 = new Epic("Epic1", "Checking create1");
        System.out.println(manager.createEpic(epic1));
        System.out.println("Создаем подзадачи");
        LocalDateTime time2 = LocalDateTime.of(2024, 2, 3, 3, 44);
        Subtask sub1 = new Subtask("Subtask1", "Checking create1", TaskStatus.DONE, 2, Duration.ofMinutes(6), time2);
        System.out.println(manager.createSubtask(sub1));
        LocalDateTime time3 = LocalDateTime.of(2024, 2, 3, 3, 30);
        Subtask sub2 = new Subtask("Subtask2", "Checking create2", TaskStatus.DONE, 2, Duration.ofMinutes(3), time3);
        System.out.println(manager.createSubtask(sub2));
        LocalDateTime time4 = LocalDateTime.of(2024, 6, 5, 5, 35);
        Subtask sub3 = new Subtask("Subtask3", "Checking create3", TaskStatus.DONE, 2, Duration.ofMinutes(35), time4);
        System.out.println(manager.createSubtask(sub3));
        System.out.println("Проверяем списки");
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtascs());
        System.out.println("Проверяем список по приоритету");
        System.out.println(manager.getPrioritizedTasks());
        System.out.println("Изменяем задачу Task1");
        LocalDateTime time10 = LocalDateTime.of(2020, 4, 11, 11, 11);
        Task task4 = new Task("Change_Task1", "Checking change", task1.getId(), TaskStatus.DONE,Duration.ofMinutes(3),time10);
        manager.updateTask(task4);
        System.out.println("Проверяем список по приоритету");
        System.out.println(manager.getPrioritizedTasks());
        System.out.println("Удаляем задачу task1");
        manager.deleteTaskById(task1.getId());
        System.out.println("Проверяем список по приоритету");
        System.out.println(manager.getPrioritizedTasks());
        System.out.println("Удаляем все подзадачи");
        manager.deleteAllSubtasks();
        System.out.println("Проверяем список по приоритету");
        System.out.println(manager.getPrioritizedTasks());
    }

    public void save() {
        try (Writer fileWriter = new FileWriter(myfile, StandardCharsets.UTF_8)) {
            fileWriter.write("id;type;name;status;description;duration;startTime;endTime;epic");
            fileWriter.write(System.lineSeparator());
            getTasks().stream()
                    .map(CSVUtil::toString)
                    .forEach(str -> {
                        try {
                            fileWriter.write(str);
                            fileWriter.write(System.lineSeparator());

                        } catch (IOException e) {
                            throw new ManagerSaveException("Произошла ошибка c обьектом Задача во время записи файла.", e);
                        }
                    });
            getEpics().stream()
                    .map(CSVUtil::toString)
                    .forEach(str -> {
                        try {
                            fileWriter.write(str);
                            fileWriter.write(System.lineSeparator());
                        } catch (IOException e) {
                            throw new ManagerSaveException("Произошла ошибка c обьектом Эпик во время записи файла.", e);
                        }
                    });
            getSubtascs().stream()
                    .map(CSVUtil::toString)
                    .forEach(str -> {
                        try {
                            fileWriter.write(str);
                            fileWriter.write(System.lineSeparator());
                        } catch (IOException e) {
                            throw new ManagerSaveException("Произошла ошибка c обьектом Подзадача во время записи файла.", e);
                        }
                    });
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время записи файла.", e);
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        TaskManager manager = Managers.getDefault();
        FileBackedTaskManager loadManager = new FileBackedTaskManager(manager.getHistoryManager(), file);

        try (BufferedReader buf = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            List<String> list = new ArrayList<>();
            String l;
            while ((l = buf.readLine()) != null) { //читаем построчно из файла
                list.add(l);
            }
            list.stream()
                    .skip(1)
                    .map(CSVUtil::fromString)
                    .filter(Objects::nonNull)
                    .forEach(ob -> {
                        if (ob.getClass() == Subtask.class) { //определяем класс обьекта
                            loadManager.createSubtaskFromFile((Subtask) ob); //создаем обьект
                        } else if (ob.getClass() == Epic.class) {
                            loadManager.createEpicFromFile((Epic) ob);
                        } else if (ob.getClass() == Task.class) {
                            loadManager.createTaskFromFile(ob);
                        } else {
                            System.out.println("Класс обьекта не был определен!");
                        }
                    });
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время чтения файла.", e);
        }
        loadManager.setNextId(InMemoryTaskManager.getMaxOldId()); //устанавливаем стартовый id у InMemoryTaskManager
        return loadManager;
    }

    @Override
    public void setNextId(int maxOldId) {
        super.setNextId(maxOldId);
    }

    @Override
    public Task createTask(Task task) {
        super.createTask(task);
        save();
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        super.createEpic(epic);
        save();
        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
        return subtask;
    }

    @Override
    public Task updateTask(Task task) {
        super.updateTask(task);
        save();
        return task;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
        return epic;
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        super.updateEpicStatus(epic);
        save();
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
        return subtask;
    }

    @Override
    public ArrayList<Task> getTasks() {
        return super.getTasks();
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return super.getEpics();
    }

    @Override
    public ArrayList<Subtask> getSubtascs() {
        return super.getSubtascs();
    }

    @Override
    public Task getTaskById(int taskId) {
        return super.getTaskById(taskId);
    }

    @Override
    public Task getEpicById(int epicId) {
        return super.getEpicById(epicId);
    }

    @Override
    public Task getSubtaskById(int subtaskId) {
        return super.getSubtaskById(subtaskId);
    }

    @Override
    public boolean deleteAllTasks() {
        boolean result = super.deleteAllTasks();
        save();
        return result;
    }

    @Override
    public boolean deleteAllEpics() {
        boolean result = super.deleteAllEpics();
        save();
        return result;
    }

    @Override
    public boolean deleteAllSubtasks() {
        boolean result = super.deleteAllSubtasks();
        save();
        return result;
    }

    @Override
    public boolean deleteTaskById(int taskId) {
        boolean result = super.deleteTaskById(taskId);
        save();
        return result;
    }

    @Override
    public boolean deleteEpicById(int epicId) {
        boolean result = super.deleteEpicById(epicId);
        save();
        return result;
    }

    @Override
    public boolean deleteSubtaskById(int subtaskId) {
        boolean result = super.deleteSubtaskById(subtaskId);
        save();
        return result;
    }

    @Override
    public HistoryManager getHistoryManager() {
        return super.getHistoryManager();
    }
}
