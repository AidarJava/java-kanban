package ru.homework.taskmanager.service;

import ru.homework.taskmanager.model.Task;

import java.util.ArrayList;

public interface HistoryManager {

    public void add(Task task);

    public ArrayList<Task> getHistory();
}
