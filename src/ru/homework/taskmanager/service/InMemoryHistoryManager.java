package ru.homework.taskmanager.service;

import ru.homework.taskmanager.model.Task;
import java.util.List;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {//проверка на null была реализована в трех методах получения по идентификатору,оставил закомментированной
        if (task==null){
            return;
        }
        if (history.size() == 10) {
            history.remove(0);
        }
        history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return List.copyOf(history);
    }

}
