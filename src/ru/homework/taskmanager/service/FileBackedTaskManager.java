package ru.homework.taskmanager.service;

import ru.homework.taskmanager.enums.TaskStatus;
import ru.homework.taskmanager.model.Epic;
import ru.homework.taskmanager.model.Subtask;
import ru.homework.taskmanager.model.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.io.FileWriter;
import java.util.List;

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
        System.out.println("Создаем задачу");
        Task task1 = new Task("Task1", "Checking create1", TaskStatus.NEW);
        System.out.println(manager.createTask(task1));
        System.out.println("Создаем эпик");
        Epic epic1 = new Epic("Epic1", "Checking create1");
        System.out.println(manager.createEpic(epic1));
        System.out.println("Создаем подзадачи");
        Subtask sub1 = new Subtask("Subtask1", "Checking create1", TaskStatus.NEW, 1);
        System.out.println(manager.createSubtask(sub1));
        Subtask sub2 = new Subtask("Subtask2", "Checking create2", TaskStatus.IN_PROGRESS, 1);
        System.out.println(manager.createSubtask(sub2));
        Subtask sub3 = new Subtask("Subtask3", "Checking create3", TaskStatus.DONE, 1);
        System.out.println(manager.createSubtask(sub3));
        System.out.println("Проверяем списки");
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtascs());
    }

    public void save() {
        try (Writer fileWriter = new FileWriter(myfile, StandardCharsets.UTF_8)) {
            fileWriter.write("id;type;name;status;description;epic");
            fileWriter.write(System.lineSeparator());
            for (Task tsk : getTasks()) {
                String str = CSVUtil.toString(tsk);
                fileWriter.write(str);
                fileWriter.write(System.lineSeparator());
            }
            for (Epic epc : getEpics()) {
                String str = CSVUtil.toString(epc);
                fileWriter.write(str);
                fileWriter.write(System.lineSeparator());
            }
            for (Subtask sub : getSubtascs()) {
                String str = CSVUtil.toString(sub);
                fileWriter.write(str);
                fileWriter.write(System.lineSeparator());
            }

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
            for (int i = 1; i < list.size(); i++) {
                if (CSVUtil.fromString(list.get(i)) != null) {
                    if (CSVUtil.fromString(list.get(i)).getClass() == Subtask.class) { //определяем класс обьекта
                        loadManager.createSubtaskFromFile((Subtask) CSVUtil.fromString(list.get(i))); //создаем обьект
                    } else if (CSVUtil.fromString(list.get(i)).getClass() == Epic.class) {
                        loadManager.createEpicFromFile((Epic) CSVUtil.fromString(list.get(i)));
                    } else if (CSVUtil.fromString(list.get(i)).getClass() == Task.class) {
                        loadManager.createTaskFromFile(CSVUtil.fromString(list.get(i)));
                    } else {
                        System.out.println("Класс обьекта не был определен!");
                    }
                } else {
                    System.out.println("Файл содержал недоступные для восстановления истории обьекты!");
                }

            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время чтения файла.", e);
        }
        loadManager.setNextId(CSVUtil.maxOldId); //устанавливаем стартовый id у InMemoryTaskManager
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
