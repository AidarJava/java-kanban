package ru.homework.taskmanager.service;

import ru.homework.taskmanager.enums.TaskStatus;
import ru.homework.taskmanager.model.Epic;
import ru.homework.taskmanager.model.Subtask;
import ru.homework.taskmanager.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class CSVUtil {
    static int maxOldId = 0; //максимальный id обьектов из файла

    public static Task fromString(String value) {
        String[] arr = value.split(";"); //разбиваем строку(в моем Exel запятая почему-то не проходит)
        if (arr[1].equals("TASK")) { //исходя из параметра type, создаем подходящий обьект
            int minutes = Integer.parseInt(arr[5]);
            Duration duration = Duration.ofMinutes(minutes);
            Task task = new Task(arr[2], arr[4], Integer.parseInt(arr[0]), TaskStatus.valueOf(arr[3]),
            duration, LocalDateTime.parse(arr[6], DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm")));
            maxOldId = Math.max(maxOldId, Integer.parseInt(arr[0]));
            return task;
        } else if (arr[1].equals("EPIC")) {
            Epic epic = new Epic(arr[2], arr[4], Integer.parseInt(arr[0]));
            maxOldId = Math.max(maxOldId, Integer.parseInt(arr[0]));
            return epic;
        } else if (arr[1].equals("SUBTASK")) {
            int minutes = Integer.parseInt(arr[5]);
            Duration duration = Duration.ofMinutes(minutes);
            Subtask subtask = new Subtask(arr[2], arr[4], Integer.parseInt(arr[0]), TaskStatus.valueOf(arr[3]), Integer.parseInt(arr[8]),
            duration, LocalDateTime.parse(arr[6], DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm")));
            maxOldId = Math.max(maxOldId, Integer.parseInt(arr[0]));
            return subtask;
        } else {
            return null;
        }
    }

    public static String toString(Task task) {
        return task.id +
                ";" + Task.type +
                ";" + task.name +
                ";" + task.status +
                ";" + task.description +
                ";" + (int) task.duration.toMinutes() +
                ";" + task.startTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm")) +
                ";" + task.startTime.plus(task.duration)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm"));

    }

    public static String toString(Epic task) {
        if (task.startTime == null) {
            return task.id +
                    ";" + Epic.type +
                    ";" + task.name +
                    ";" + task.status +
                    ";" + task.description;
        }
        return task.id +
                ";" + Epic.type +
                ";" + task.name +
                ";" + task.status +
                ";" + task.description +
                ";" + (int) task.duration.toMinutes() +
                ";" + task.startTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm")) +
                ";" + task.endTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm"));
    }

    public static String toString(Subtask task) {
        return task.id +
                ";" + Subtask.type +
                ";" + task.name +
                ";" + task.status +
                ";" + task.description +
                ";" + (int) task.duration.toMinutes() +
                ";" + task.startTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm")) +
                ";" + task.startTime.plus(task.duration)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm")) +
                ";" + task.getEpicId();
    }
}
