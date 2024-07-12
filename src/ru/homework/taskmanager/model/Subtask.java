package ru.homework.taskmanager.model;

import ru.homework.taskmanager.enums.TaskStatus;
import ru.homework.taskmanager.enums.TaskType;

import static ru.homework.taskmanager.enums.TaskType.SUBTASK;

public class Subtask extends Task {
    private int epicId;
    public static final TaskType type = SUBTASK;

    public Subtask(String name, String description, TaskStatus status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, Integer id, TaskStatus status, int epicId) {
        super(name, description, id, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", epicId=" + epicId +
                '}';
    }
}

