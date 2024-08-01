package ru.homework.taskmanager.service;

import ru.homework.taskmanager.enums.TaskStatus;
import ru.homework.taskmanager.model.Epic;
import ru.homework.taskmanager.model.Subtask;
import ru.homework.taskmanager.model.Task;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {

    private static int maxOldId; //максимальный id обьектов из файла
    private static int nextId = maxOldId;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    HistoryManager historyManager;

    Comparator<Task> comparator = new Comparator<Task>() {
        @Override
        public int compare(Task task1, Task task2) {
            return task1.startTime.compareTo(task2.startTime);
        }
    };
    private final Set<Task> prioritizedTasks = new TreeSet<>(comparator);

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;

    }

    public static void setMaxOldId(int maxOldId) {
        InMemoryTaskManager.maxOldId = maxOldId;
    }

    public static int getMaxOldId() {
        return maxOldId;
    }

    private int getNextId() {
        return nextId++;
    }

    public void setNextId(int nextId) {
        this.nextId = nextId + 1;
    }

    //_____________________проверки на пересечение______________________________________
    @Override
    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    boolean checkIntersection(Task task1, Task task2) { //проверка на пересечение 2-х задач (true-нет пересечений)
        boolean start1LowerStart2AndEnd1LowerStart2 = task1.startTime.isBefore(task2.startTime.plus(task2.duration))
                && task1.startTime.plus(task1.duration).isBefore(task2.startTime);
        boolean start2LowerStart1AndEnd2LowerStart1 = task2.startTime.isBefore(task1.startTime.plus(task1.duration))
                && task2.startTime.plus(task2.duration).isBefore(task1.startTime);
        return (start1LowerStart2AndEnd1LowerStart2 || start2LowerStart1AndEnd2LowerStart1);
    }

    public boolean checkToaddNoIntersectTask(Task task) { //проверка на пересечение со старыми задачами
        Optional<Boolean> intersection = getPrioritizedTasks().stream()
                .map(oldTask -> checkIntersection(task, oldTask))
                .filter(bool -> bool == false)
                .findFirst();
        if (intersection.isPresent()) {
            System.out.println("В данной задаче время выполнения пересекается с уже существующими задачами!");
            return false;
        }
        return true;
    }

    //_____________________создание списков______________________________________
    @Override
    public Task createTask(Task task) {
        if (checkToaddNoIntersectTask(task)) {
            task.setId(getNextId());
            createTaskFromFile(task);
            if (task.startTime != null) { //если дата начала указана добавляем в список
                prioritizedTasks.add(task);
            }
            return task;
        }
        return null;
    }

    public Task createTaskFromFile(Task task) {
        tasks.put(task.getId(), task);
        if (task.startTime != null) { //если дата начала указана добавляем в список
            prioritizedTasks.add(task);
        }
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(getNextId());
        epics.put(epic.getId(), epic);
        updateEpic(epic);
        updateEpicStatus(epic);
        return epic;
    }

    public Epic createEpicFromFile(Epic epic) {
        epics.put(epic.getId(), epic);
        updateEpic(epic);
        updateEpicStatus(epic);
        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        if (checkToaddNoIntersectTask(subtask)) {
            if (!epics.containsKey(subtask.getEpicId())) {
                return null;
            }
            subtask.setId(getNextId());
            subtasks.put(subtask.getId(), subtask);
            updateEpic(epics.get(subtask.getEpicId())); //обновляем эпик и добавляем в него эту подзадачу
            updateEpicStatus(epics.get(subtask.getEpicId()));
            if (subtask.startTime != null) { //если дата начала указана добавляем в список
                prioritizedTasks.add(subtask);
            }
            return subtask;
        }
        return null;
    }

    public Subtask createSubtaskFromFile(Subtask subtask) {
        if (!epics.containsKey(subtask.getEpicId())) {
            return null;
        }
        subtasks.put(subtask.getId(), subtask);
        updateEpic(epics.get(subtask.getEpicId()));//обновляем эпик и добавляем в него эту подзадачу
        updateEpicStatus(epics.get(subtask.getEpicId()));
        if (subtask.startTime != null) { //если дата начала указана добавляем в список
            prioritizedTasks.add(subtask);
        }
        return subtask;
    }

    //_____________________обновление списков______________________________________
    @Override
    public Task updateTask(Task task) {
        if (task.getId() == null || !tasks.containsKey(task.getId())) {
            return null;
        }
        prioritizedTasks.remove(getTaskById(task.getId()));
        tasks.put(task.getId(), task);
        prioritizedTasks.add(task);
        return task;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        if (epic.getId() == null || !epics.containsKey(epic.getId())) {
            return null;
        }
        if (!subtasks.isEmpty()) {
            List<Integer> subtaskIdToEpic = subtasks.values().stream()
                    .filter(sub -> sub.getEpicId() == epic.getId())
                    .map(sub -> sub.getId()).distinct()
                    .collect(Collectors.toList());
            epic.setSubtaskId(new ArrayList<>(subtaskIdToEpic));
        }
        updateEpicStatus(epic);
        getEndTime(epic);
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public void getEndTime(Epic epic) {
        if (epic.getId() == null || !epics.containsKey(epic.getId())) {
            return;
        }
        epic.setEndTime(null);//устанавливаем время завершения эпика если нет поздадач
        epic.startTime = null;
        epic.duration = Duration.ofMinutes(0);
        if (!epic.getSubtaskId().isEmpty()) {
            epic.getSubtaskId().stream()
                    .map(subtasks::get)
                    .forEach(subtask -> {
                        if (epic.getEndTime() == null || epic.getEndTime().isBefore(subtask.startTime.plus(subtask.duration))) { //если время начала + продолжительность подзадачи дальше
                            epic.setEndTime(subtask.startTime.plus(subtask.duration)); //если зашли, то меняем время завершения эпика
                        }
                        if (epic.startTime == null || epic.startTime.isAfter(subtask.startTime)) {
                            epic.startTime = subtask.startTime;
                        }
                        epic.duration = epic.duration.plus(subtask.duration);
                    });
        }
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        if (epic.getId() == null || !epics.containsKey(epic.getId())) {
            return;
        }
        epic.status = TaskStatus.NEW;//устанавливаем статус эпика если нет поздадач
        if (!epic.getSubtaskId().isEmpty()) {
            epic.status = TaskStatus.DONE;//авансом устанавливаем статус эпика
            Optional<Subtask> subtaskStream = epic.getSubtaskId().stream()
                    .map(subtasks::get)
                    .filter(sub -> sub.getStatus() == TaskStatus.IN_PROGRESS || sub.getStatus() == TaskStatus.NEW) //фильтруем подзадачи по статусам
                    .findFirst();
            if (subtaskStream.isPresent()) { //если остался, то меняем статус эпика
                epic.status = TaskStatus.IN_PROGRESS;
            }
        }
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        if (subtask.getId() == null || !subtasks.containsKey(subtask.getId()) || !epics.containsKey(subtask.getEpicId())) {
            return null;
        }
        prioritizedTasks.remove(getSubtaskById((subtask.getId())));
        subtasks.put(subtask.getId(), subtask);
        updateEpic(epics.get(subtask.getEpicId()));
        updateEpicStatus(epics.get(subtask.getEpicId()));
        prioritizedTasks.add(subtask);
        return subtask;
    }

    // ___________________получение списков______________________________________
    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getSubtascs() {
        return new ArrayList<>(subtasks.values());
    }

    //_____________________получение задач по идентификатору______________________________________
    @Override
    public Task getTaskById(int taskId) {
        historyManager.add(tasks.get(taskId));
        return tasks.get(taskId);
    }

    @Override
    public Task getEpicById(int epicId) {
        historyManager.add(epics.get(epicId));
        return epics.get(epicId);
    }

    @Override
    public Task getSubtaskById(int subtaskId) {
        historyManager.add(subtasks.get(subtaskId));
        return subtasks.get(subtaskId);
    }

    //_____________________удаление списков______________________________________
    @Override
    public boolean deleteAllTasks() {
        tasks.values().stream()
                .forEach(tas -> prioritizedTasks.remove(tas));
        tasks.clear();
        return tasks.isEmpty();
    }

    @Override
    public boolean deleteAllEpics() {
        subtasks.clear();
        epics.clear();
        return epics.isEmpty() && subtasks.isEmpty();
    }

    @Override
    public boolean deleteAllSubtasks() {
        subtasks.values().stream()
                .forEach(sub -> prioritizedTasks.remove(sub));
        subtasks.clear();
        epics.values()
                .stream()
                .peek(epic -> epic.getSubtaskId().clear())
                .forEach(epic -> {
                    updateEpic(epic);
                    updateEpicStatus(epic);
                });
        return subtasks.isEmpty();
    }

    //_____________________удаление по идентификатору______________________________________
    @Override
    public boolean deleteTaskById(int taskId) {
        if (!tasks.containsKey(taskId)) {
            return false;
        }
        prioritizedTasks.remove(getTaskById(taskId));
        tasks.remove(taskId);
        historyManager.remove(taskId);
        return !tasks.containsKey(taskId);
    }

    @Override
    public boolean deleteEpicById(int epicId) {
        if (!epics.containsKey(epicId)) {
            return false;
        }
        if (!epics.get(epicId).getSubtaskId().isEmpty()) { //проверка, что у эпика есть подзадачи

            epics.get(epicId).getSubtaskId()
                    .stream()
                    .forEach(num -> {
                        prioritizedTasks.remove(getSubtaskById(num));
                        subtasks.remove(num);
                        historyManager.remove(num);
                    });
        }
        epics.remove(epicId);
        historyManager.remove(epicId);
        return !epics.containsKey(epicId);
    }

    @Override
    public boolean deleteSubtaskById(int subtaskId) {
        if (!subtasks.containsKey(subtaskId)) {
            return false;
        }
        int numEpicId = subtasks.get(subtaskId).getEpicId();//номер эпика, в котором лежит подзадача
        epics.get(numEpicId).getSubtaskId().remove(Integer.valueOf(subtaskId));//удаляем подзадачу из аррей листа эпика
        prioritizedTasks.remove(getSubtaskById(subtaskId));
        subtasks.remove(subtaskId);
        historyManager.remove(subtaskId);
        updateEpic(epics.get(numEpicId));//обновление эпика
        updateEpicStatus(epics.get(numEpicId));
        return !subtasks.containsKey(subtaskId);
    }

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }
}
