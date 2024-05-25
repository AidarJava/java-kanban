package ru.homework.taskmanager.service;

import ru.homework.taskmanager.enums.TaskStatus;
import ru.homework.taskmanager.model.Epic;
import ru.homework.taskmanager.model.Subtask;
import ru.homework.taskmanager.model.Task;

import java.util.HashMap;
import java.util.ArrayList;

public class TaskManager {
    private int nextId;

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();

    private int getNextId() {
        return nextId++;
    }

    //_____________________создание списков______________________________________
    public Task createTask(Task task) {
        task.setId(getNextId());
        tasks.put(task.getId(), task);
        return task;
    }

    public Epic createEpic(Epic epic) {
        epic.setId(getNextId());
        epics.put(epic.getId(), epic);
        updateEpic(epic);
        updateEpicStatus(epic);
        return epic;
    }

    public Subtask createSubtask(Subtask subtask) {
        if (!epics.containsKey(subtask.getEpicId())) {
            return null;
        }
        subtask.setId(getNextId());
        subtasks.put(subtask.getId(), subtask);
        updateEpic(epics.get(subtask.getEpicId()));//обновляем эпик и добавляем в него эту подзадачу
        updateEpicStatus(epics.get(subtask.getEpicId()));
        return subtask;
    }

    //_____________________обновление списков______________________________________
    public Task updateTask(Task task) {
        if (task.getId() == null || !tasks.containsKey(task.getId())) {
            return null;
        }
        tasks.put(task.getId(), task);
        return task;
    }

    public Epic updateEpic(Epic epic) {
        if (epic.getId() == null || !epics.containsKey(epic.getId())) {
            return null;
        }
        ArrayList<Integer> subtaskIdToEpic = new ArrayList<>();
        if (!subtasks.isEmpty()) {
            for (Subtask sub : subtasks.values()) {
                if (sub.getEpicId() == epic.getId() && !subtaskIdToEpic.contains(sub.id)) {//2-е условие-проверка на дублирование
                    subtaskIdToEpic.add(sub.id);//добавлям id подзадачи в эпик
                }
                epic.setSubtaskId(subtaskIdToEpic);
            }
        }
        epics.put(epic.getId(), epic);
        return epic;
    }

    public void updateEpicStatus(Epic epic) {
        if (epic.getId() == null || !epics.containsKey(epic.getId())) {
            return;
        }
        epic.status = TaskStatus.NEW;//устанавливаем статус эпика если нет поздадач
        if (!epic.getSubtaskId().isEmpty()) {
            epic.status = TaskStatus.DONE;//авансом устанавливаем статус эпика
            for (Integer i : epic.getSubtaskId()) {
                if (subtasks.get(i).status == TaskStatus.IN_PROGRESS || subtasks.get(i).status == TaskStatus.NEW) {//проверяем статус подзадач
                    epic.status = TaskStatus.IN_PROGRESS;//если зашли, то меняем статус эпика
                }
            }
        }
    }

    public Subtask updateSubtask(Subtask subtask) {
        if (subtask.getId() == null || !subtasks.containsKey(subtask.getId()) || !epics.containsKey(subtask.getEpicId())) {
            return null;
        }
        subtasks.put(subtask.getId(), subtask);
        updateEpic(epics.get(subtask.getEpicId()));
        updateEpicStatus(epics.get(subtask.getEpicId()));
        return subtask;
    }

    // ___________________получение списков______________________________________
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getSubtascs() {
        return new ArrayList<>(subtasks.values());
    }

    //_____________________получение задач по идентификатору______________________________________
    public Task getTaskById(int taskId) {
        return tasks.get(taskId);
    }

    public Task getEpicById(int epicId) {
        return epics.get(epicId);
    }

    public Task getSubtaskById(int subtaskId) {
        return subtasks.get(subtaskId);
    }

    //_____________________удаление списков______________________________________
    public boolean deleteAllTasks() {
        tasks.clear();
        return tasks.isEmpty();
    }

    public boolean deleteAllEpics() {
        subtasks.clear();
        epics.clear();
        return epics.isEmpty() && subtasks.isEmpty();
    }

    public boolean deleteAllSubtasks() {
        subtasks.clear();
        for (Epic ep : epics.values()) {
            ep.getSubtaskId().clear();//очищаем все аррай листы с номерами подзадач
            updateEpic(ep);//обновление эпика
            updateEpicStatus(ep);//обновление статуса эпика
        }
        return subtasks.isEmpty();
    }

    //_____________________удаление по идентификатору______________________________________
    public boolean deleteTaskById(int taskId) {
        if (!tasks.containsKey(taskId)) {
            return false;
        }
        tasks.remove(taskId);
        return !tasks.containsKey(taskId);
    }

    public boolean deleteEpicById(int epicId) {
        if (!epics.containsKey(epicId)) {
            return false;
        }
        if (!epics.get(epicId).getSubtaskId().isEmpty()) {//проверка, что у эпика есть подзадачи
            for (Integer idSub : epics.get(epicId).getSubtaskId()) {
                subtasks.remove(idSub);//удаляем подзадачи этого эпика
            }
        }
        epics.remove(epicId);
        return !epics.containsKey(epicId);
    }

    public boolean deleteSubtaskById(int subtaskId) {
        if (!subtasks.containsKey(subtaskId)) {
            return false;
        }
        int numEpicId = subtasks.get(subtaskId).getEpicId();//номер эпика, в котором лежит подзадача
        epics.get(numEpicId).getSubtaskId().remove(Integer.valueOf(subtaskId));//удаляем подзадачу из аррей листа эпика
        subtasks.remove(subtaskId);
        updateEpic(epics.get(numEpicId));//обновление эпика
        updateEpicStatus(epics.get(numEpicId));
        return !subtasks.containsKey(subtaskId);
    }

}
