package ru.homework.taskmanager.service;

import ru.homework.taskmanager.model.Node;
import ru.homework.taskmanager.model.Task;

import java.util.List;
import java.util.Map;

public interface HistoryManager {

    public void add(Task task);

    void remove(int id);

    public List<Task> getHistory();

}
