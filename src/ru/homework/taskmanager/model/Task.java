package ru.homework.taskmanager.model;

import ru.homework.taskmanager.enums.TaskStatus;
import ru.homework.taskmanager.enums.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static ru.homework.taskmanager.enums.TaskType.TASK;

public class Task {
    public String name;
    public String description;
    public Integer id;
    public TaskStatus status;
    private static final TaskType type = TASK;
    public Duration duration;
    public LocalDateTime startTime;
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Task(String name, String description, Integer id) {
        this.name = name;
        this.description = description;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public Task(String name, String description, Integer id, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description, TaskStatus status, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String name, String description, Integer id, TaskStatus status, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return this.startTime.plus(this.duration);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(type).append("{")
                .append("name='").append(name).append('\'')
                .append(", description='").append(description).append('\'')
                .append(", id=").append(id)
                .append(", status=").append(status)
                .append(", duration=").append(duration);

        if (startTime != null) {
            str.append(", startTime=").append(startTime.format(FORMATTER))
                    .append(", endTime=").append(startTime.plus(duration).format(FORMATTER));
        }

        str.append('}');
        return str.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

