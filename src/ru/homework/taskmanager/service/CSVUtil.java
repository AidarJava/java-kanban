package ru.homework.taskmanager.service;

import ru.homework.taskmanager.enums.TaskStatus;
import ru.homework.taskmanager.enums.TaskType;
import ru.homework.taskmanager.model.Epic;
import ru.homework.taskmanager.model.Subtask;
import ru.homework.taskmanager.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class CSVUtil {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");

    public static Task fromString(String value) {
        String[] arr = value.split(";"); //разбиваем строку(в моем Exel запятая почему-то не проходит)
        Integer id = Integer.parseInt(arr[0]);
        String type = arr[1];
        String name = arr[2];
        TaskStatus status = TaskStatus.valueOf(arr[3]);
        String description = arr[4];
        int maxOldId = Math.max(InMemoryTaskManager.getMaxOldId(), Integer.parseInt(arr[0]));
        InMemoryTaskManager.setMaxOldId(maxOldId);
        switch (type) {
            case "TASK": //исходя из параметра type, создаем подходящий обьект
                Duration durationOfMinutes = Duration.ofMinutes(Integer.parseInt(arr[5]));
                LocalDateTime startTime = LocalDateTime.parse(arr[6], FORMATTER);
                return new Task(name, description, id, status, durationOfMinutes, startTime);

            case "EPIC":
                return new Epic(name, description, id);

            case "SUBTASK":
                durationOfMinutes = Duration.ofMinutes(Integer.parseInt(arr[5]));
                startTime = LocalDateTime.parse(arr[6], FORMATTER);
                int epicId = Integer.parseInt(arr[8]);
                return new Subtask(name, description, id, status, epicId, durationOfMinutes, startTime);

            default:
                return null;

        }
    }

    public static String toString(Task task) {
        return task.id +
                ";" + TaskType.TASK +
                ";" + task.name +
                ";" + task.status +
                ";" + task.description +
                ";" + (int) task.duration.toMinutes() +
                ";" + task.startTime.format(FORMATTER) +
                ";" + task.startTime.plus(task.duration).format(FORMATTER);

    }

    public static String toString(Epic task) {
        StringBuilder str = new StringBuilder();
        str.append(task.id).append(";")
                .append(TaskType.EPIC).append(";")
                .append(task.name).append(";")
                .append(task.status).append(";")
                .append(task.description);

        if (task.startTime != null) {
            str.append(";").append((int) task.duration.toMinutes())
                    .append(";").append(task.startTime.format(FORMATTER))
                    .append(";").append(task.getEndTime().format(FORMATTER));
        }

        return str.toString();
    }


    public static String toString(Subtask task) {
        return task.id +
                ";" + TaskType.SUBTASK +
                ";" + task.name +
                ";" + task.status +
                ";" + task.description +
                ";" + (int) task.duration.toMinutes() +
                ";" + task.startTime.format(FORMATTER) +
                ";" + task.startTime.plus(task.duration).format(FORMATTER) +
                ";" + task.getEpicId();
    }
}
