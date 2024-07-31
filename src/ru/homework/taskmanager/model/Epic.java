package ru.homework.taskmanager.model;

import ru.homework.taskmanager.enums.TaskType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static ru.homework.taskmanager.enums.TaskType.EPIC;

public class Epic extends Task {
    private ArrayList<Integer> subtaskId = new ArrayList<>();
    public static final TaskType type = EPIC;
    public LocalDateTime endTime;

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
        if(startTime==null){
            return "Epic{" +
                    "name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    ", id=" + id +
                    ", status=" + status +
                    ", subtaskId=" + subtaskId +
                    '}';
        }
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", subtaskId=" + subtaskId +
                ", duration=" +duration +
                ", startTime=" + startTime.format(formater) +
                ", endTime=" + startTime.plus(duration)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm"))+
                '}';
    }
}

