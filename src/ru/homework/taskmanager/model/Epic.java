package ru.homework.taskmanager.model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtaskId = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(String name, String description, Integer id) {
        super(name, description, id);
    }

    public ArrayList<Integer> getSubtaskId() {
        return subtaskId;
    }

    public void setSubtaskId(ArrayList<Integer> subtaskId) {
        this.subtaskId = subtaskId;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", subtaskId=" + subtaskId +
                '}';
    }
}

