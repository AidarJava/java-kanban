package ru.homework.taskmanager.service;

import ru.homework.taskmanager.model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    public static ArrayList<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (history.size() == 10) {
            history.remove(0);
        }
        history.add(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }

}
