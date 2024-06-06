package ru.homework.taskmanager.service;

import ru.homework.taskmanager.model.Task;

import java.util.List;

public interface HistoryManager {

    public void add(Task task);

    public List<Task> getHistory();
}
