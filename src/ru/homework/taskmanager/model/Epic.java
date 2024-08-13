package ru.homework.taskmanager.model;

import ru.homework.taskmanager.enums.TaskType;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static ru.homework.taskmanager.enums.TaskType.EPIC;

public class Epic extends Task {

    private static final TaskType type = EPIC;
    private LocalDateTime endTime;
    private ArrayList<Integer> subtaskId = new ArrayList<>();

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

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
        StringBuilder str = new StringBuilder();
        str.append(type).append("{")
                .append("name='").append(name).append('\'')
                .append(", description='").append(description).append('\'')
                .append(", id=").append(id)
                .append(", status=").append(status)
                .append(", subtaskId=").append(subtaskId);

        if (startTime != null) {
            str.append(", duration=").append(duration)
                    .append(", startTime=").append(startTime.format(FORMATTER))
                    .append(", endTime=").append(startTime.plus(duration).format(FORMATTER));
        }

        str.append('}');
        return str.toString();
    }

    public ArrayList<Integer> getSubtasks() {
        return subtaskId;
    }
}

