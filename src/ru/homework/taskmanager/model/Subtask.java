package ru.homework.taskmanager.model;

import ru.homework.taskmanager.enums.TaskStatus;
import ru.homework.taskmanager.enums.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.homework.taskmanager.enums.TaskType.SUBTASK;

public class Subtask extends Task {
    private int epicId;
    public static final TaskType type = SUBTASK;

    public Subtask(String name, String description, TaskStatus status, int epicId, Duration duration, LocalDateTime startTime) {
        super(name, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, Integer id, TaskStatus status, int epicId, Duration duration, LocalDateTime startTime) {
        super(name, description, id, status, duration, startTime);
        this.epicId = epicId;
    }

    public LocalDateTime getEndTime(Subtask subtask) {
        return super.getEndTime(subtask);
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
                ", duration=" + duration +
                ", startTime=" + startTime.format(formater) +
                ", endTime=" + startTime.plus(duration)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm")) +
                '}';
    }
}

