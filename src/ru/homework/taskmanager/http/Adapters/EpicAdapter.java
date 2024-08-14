package ru.homework.taskmanager.http.Adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import ru.homework.taskmanager.model.Epic;
import ru.homework.taskmanager.service.CSVUtil;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EpicAdapter extends TypeAdapter<Epic> {

    @Override
    public void write(JsonWriter jsonWriter, Epic epic) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("id").value(epic.getId());
        jsonWriter.name("name").value(epic.getName());
        jsonWriter.name("description").value(epic.getDescription());
        jsonWriter.name("status").value(epic.getStatus().toString());

        if (epic.getDuration() != null) {
            jsonWriter.name("duration").value(epic.getDuration().toString());
        } else {
            jsonWriter.name("duration").nullValue();
        }

        if (epic.getStartTime() != null) {
            jsonWriter.name("startTime").value(epic.getStartTime().format(CSVUtil.FORMATTER));
        } else {
            jsonWriter.name("startTime").nullValue();
        }

        if (epic.getEndTime() != null) {
            jsonWriter.name("endTime").value(epic.getEndTime().format(CSVUtil.FORMATTER));
        } else {
            jsonWriter.name("endTime").nullValue();
        }

        jsonWriter.name("subtasks").beginArray();
        for (Integer subtaskId : epic.getSubtasks()) {
            jsonWriter.value(subtaskId);
        }
        jsonWriter.endArray();

        jsonWriter.endObject();
    }

    @Override
    public Epic read(JsonReader jsonReader) throws IOException {
        jsonReader.beginObject();
        String name = null;
        String description = null;
        LocalDateTime startTime = null;
        Duration duration = null;
        LocalDateTime endTime = null;

        while (jsonReader.hasNext()) {
            String fieldName = jsonReader.nextName();
            switch (fieldName) {
                case "name":
                    name = jsonReader.nextString();
                    break;
                case "description":
                    description = jsonReader.nextString();
                    break;
                case "startTime":
                    String startTimeStr = jsonReader.nextString();
                    if (startTimeStr != null) {
                        startTime = LocalDateTime.parse(startTimeStr, CSVUtil.FORMATTER);
                    } else {
                        startTime = null;
                    }
                    break;
                case "duration":
                    String durationStr = jsonReader.nextString();
                    if (durationStr != null) {
                        duration = Duration.parse(durationStr);
                    } else {
                        duration = null;
                    }
                    break;
                case "endTime":
                    String endTimeStr = jsonReader.nextString();
                    if (endTimeStr != null) {
                        endTime = LocalDateTime.parse(endTimeStr, CSVUtil.FORMATTER);
                    } else {
                        startTime = null;
                    }
                    break;
                case "subtasks":
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {
                        List<Integer> subtasks = new ArrayList<>();
                        subtasks.add(jsonReader.nextInt());
                    }
                    jsonReader.endArray();
                    break;
                default:
                    jsonReader.skipValue();
                    break;
            }
        }
        jsonReader.endObject();
        return new Epic(name, description);
    }
}

