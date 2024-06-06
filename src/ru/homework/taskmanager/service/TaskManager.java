package ru.homework.taskmanager.service;

import ru.homework.taskmanager.model.Epic;
import ru.homework.taskmanager.model.Subtask;
import ru.homework.taskmanager.model.Task;

import java.util.List;

public interface TaskManager{
    //_____________________создание списков______________________________________
    Task createTask(Task task);

    Epic createEpic(Epic epic);

    Subtask createSubtask(Subtask subtask);

    //_____________________обновление списков______________________________________
    Task updateTask(Task task);

    Epic updateEpic(Epic epic);

    void updateEpicStatus(Epic epic);

    Subtask updateSubtask(Subtask subtask);

    // ___________________получение списков______________________________________
    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubtascs();

    //_____________________получение задач по идентификатору______________________________________
    Task getTaskById(int taskId);

    Task getEpicById(int epicId);

    Task getSubtaskById(int subtaskId);

    //_____________________удаление списков______________________________________
    boolean deleteAllTasks();

    boolean deleteAllEpics();

    boolean deleteAllSubtasks();

    //_____________________удаление по идентификатору______________________________________
    boolean deleteTaskById(int taskId);

    boolean deleteEpicById(int epicId);

    boolean deleteSubtaskById(int subtaskId);

    HistoryManager getHistoryManager();
}
