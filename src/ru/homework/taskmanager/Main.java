package ru.homework.taskmanager;

import ru.homework.taskmanager.model.Epic;
import ru.homework.taskmanager.model.Subtask;
import ru.homework.taskmanager.model.Task;
import ru.homework.taskmanager.enums.TaskStatus;
import ru.homework.taskmanager.service.*;

public class Main {

    public static void main(String[] args) {
        testingTask();
    }

    public static void testingTask() {
        TaskManager manager = Managers.getDefault();
        TaskManager manager2 = Managers.getDefault();

        System.out.println("Проверка на создание задач,эпиков и подзадач");
        //_______________________________проверка работы с тасками___________________________________________
        System.out.println("Создаем задачи");
        Task task1 = new Task("Задача1", "Проверка создания1", TaskStatus.NEW);
        System.out.println(manager.createTask(task1));
        Task task2 = new Task("Задача2", "Проверка создания2", TaskStatus.IN_PROGRESS);
        System.out.println(manager.createTask(task2));
        Task task3 = new Task("Задача3", "Проверка создания3", TaskStatus.DONE);
        System.out.println(manager.createTask(task3));
        System.out.println("Проверяем список задач");
        System.out.println(manager.getTasks());
        System.out.println("Изменяем задачу 2");
        Task task4 = new Task("Изм_задачи2", "Проверка изменения", task2.getId(), TaskStatus.DONE);
        manager.updateTask(task4);
        System.out.println(manager.getTasks());
        System.out.println("Получаем задачу по id=)");
        System.out.println(manager.getTaskById(1));
        System.out.println("Удаляем задачу по id=1");
        System.out.println("Результат удаления задачи с id=1: " + manager.deleteTaskById(1));
        System.out.println("Проверяем список задач");
        System.out.println(manager.getTasks());
        System.out.println("Удаляем все задачи");
        System.out.println("Результат удаления всего списка задач: " + manager.deleteAllTasks());
        System.out.println("Проверяем список задач");
        System.out.println(manager.getTasks());
        System.out.println("_________________________________________________________________________________");
//__________проверка работы с эпиками (изменение статуса проверяется при проверке работы с подзадачами)____________
        System.out.println("Создаем эпики");
        Epic epic1 = new Epic("Эпик1", "Проверка создания1");
        System.out.println(manager.createEpic(epic1));
        Epic epic2 = new Epic("Эпик2", "Проверка создания2");
        System.out.println(manager.createEpic(epic2));
        System.out.println("Проверяем список эпиков");
        System.out.println(manager.getEpics());
        System.out.println("Изменяем эпик 1");
        Epic epic3 = new Epic("Изм_эпика1", "Проверка изменения", epic1.getId());
        manager.updateEpic(epic3);
        System.out.println(manager.getEpics());
        System.out.println("Получаем эпик по id=4");
        System.out.println(manager.getEpicById(4));
        System.out.println("Удаляем эпик по id=4");
        System.out.println("Результат удаления эпика с id=4: " + manager.deleteEpicById(4));
        System.out.println("Проверяем список эпиков");
        System.out.println(manager.getEpics());
        System.out.println("Удаляем все эпики");
        System.out.println("Результат удаления всего списка эпиков: " + manager.deleteAllEpics());
        System.out.println("Проверяем список эпиков");
        System.out.println(manager.getEpics());
        System.out.println("_________________________________________________________________________________");
//_______________________________проверка работы с подзадачами___________________________________________
        System.out.println("Создаем эпики, иначе добавить в список подзадачу не получится");
        Epic epic4 = new Epic("Эпик4", "Проверка проверка работы с подзадачами1");
        System.out.println(manager.createEpic(epic4));
        Epic epic5 = new Epic("Эпик5", "Проверка проверка работы с подзадачами2");
        System.out.println(manager.createEpic(epic5));
        System.out.println("Создаем подзадачи");
        Subtask sub1 = new Subtask("Подзадача1", "Проверка создания1", TaskStatus.NEW, 5);
        System.out.println(manager.createSubtask(sub1));
        Subtask sub2 = new Subtask("Подзадача2", "Проверка создания2", TaskStatus.IN_PROGRESS, 5);
        System.out.println(manager.createSubtask(sub2));
        Subtask sub3 = new Subtask("Подзадача3", "Проверка создания3", TaskStatus.DONE, 6);
        System.out.println(manager.createSubtask(sub3));
        Subtask sub4 = new Subtask("Подзадача4", "Проверка создания4", TaskStatus.IN_PROGRESS, 6);
        System.out.println(manager.createSubtask(sub4));
        System.out.println("Проверяем список подзадач");
        System.out.println(manager.getSubtascs());
        System.out.println("Изменяем подзадачу 1");
        Subtask sub5 = new Subtask("Изм_подз1", "Проверка изменения", sub1.getId(), TaskStatus.DONE, 5);
        manager.updateSubtask(sub5);
        System.out.println(manager.getSubtascs());
        System.out.println("Изменяем подзадачу 2");
        Subtask sub6 = new Subtask("Изм_подз2", "Проверка изменения2", sub2.getId(), TaskStatus.DONE, 5);
        manager.updateSubtask(sub6);
        System.out.println(manager.getSubtascs());
        System.out.println("Проверяем список эпиков и проверяем статус");
        System.out.println(manager.getEpics());
        System.out.println("Удаляем эпик по id=6");
        System.out.println("Результат удаления эпика с id=6: " + manager.deleteEpicById(6));
        System.out.println("Проверяем список эпиков");
        System.out.println(manager.getEpics());
        System.out.println("Проверяем список подзадач(должны удалится те, что были в удаленном эпике)");
        System.out.println(manager.getSubtascs());
        System.out.println("Получаем подзадачу по id=8");
        System.out.println(manager.getSubtaskById(8));
        System.out.println("Удаляем подзадачу по id=8");
        System.out.println("Результат удаления подзадачу с id=8: " + manager.deleteSubtaskById(8));
        System.out.println("Проверяем список подзадач");
        System.out.println(manager.getSubtascs());
        System.out.println("Проверяем список эпиков, на изменение входящих в него подзадач");
        System.out.println(manager.getEpics());
        System.out.println("Удаляем все подзадачи");
        System.out.println("Результат удаления всего списка подзадач: " + manager.deleteAllSubtasks());
        System.out.println("Проверяем список подзадач");
        System.out.println(manager.getSubtascs());
        System.out.println("Проверяем список эпиков, на изменение входящих в него подзадач");
        System.out.println(manager.getEpics());
        System.out.println("_________________________________________________________________________________");
//_______________________________проверка работы сохранения истории___________________________________________
        System.out.println("Создаем задачи");
        Task task6 = new Task("Задача1", "Проверка сохранения истории задачи1", TaskStatus.NEW);
        System.out.println(manager.createTask(task6));
        Task task7 = new Task("Задача2", "Проверка сохранения истории задачи2", TaskStatus.IN_PROGRESS);
        System.out.println(manager.createTask(task7));
        Task task8 = new Task("Задача3", "Проверка сохранения истории задачи3", TaskStatus.DONE);
        System.out.println(manager.createTask(task8));
        System.out.println("Создаем эпики");
        Epic epic6 = new Epic("Эпик1", "Проверка сохранения истории эпика1");
        System.out.println(manager.createEpic(epic6));
        Epic epic7 = new Epic("Эпик2", "Проверка сохранения истории эпика2");
        System.out.println(manager.createEpic(epic7));
        System.out.println("Создаем подзадачи");
        Subtask sub7 = new Subtask("Подзадача1", "Проверка сохранения истории подзадачи1", TaskStatus.DONE, 14);
        System.out.println(manager.createSubtask(sub7));
        Subtask sub8 = new Subtask("Подзадача2", "Проверка сохранения истории подзадачи2", TaskStatus.DONE, 14);
        System.out.println(manager.createSubtask(sub8));
        Subtask sub9 = new Subtask("Подзадача3", "Проверка сохранения истории подзадачи3", TaskStatus.DONE, 14);
        System.out.println(manager.createSubtask(sub9));
        Subtask sub10 = new Subtask("Подзадача4", "Проверка сохранения истории подзадачи4", TaskStatus.DONE, 15);
        System.out.println(manager.createSubtask(sub10));
        Subtask sub11 = new Subtask("Подзадача5", "Проверка сохранения истории подзадачи5", TaskStatus.DONE, 15);
        System.out.println(manager.createSubtask(sub11));
        Subtask sub12 = new Subtask("Подзадача6", "Проверка сохранения истории подзадачи6", TaskStatus.DONE, 15);
        System.out.println(manager.createSubtask(sub12));
        System.out.println("Заполняем список истории просмотров");
        for (int i = 11; i <= 13; i++) {
            System.out.println(manager.getTaskById(i));
        }
        for (int i = 14; i <= 15; i++) {
            System.out.println(manager.getEpicById(i));
        }
        for (int i = 16; i <= 20; i++) {
            System.out.println(manager.getSubtaskById(i));
        }
        System.out.println("Проверяем историю просмотров");
        System.out.println(manager.getHistoryManager().getHistory());
        System.out.println("Получаем подзадачу по id=21)");
        System.out.println(manager.getSubtaskById(21));
        System.out.println("Проверяем историю просмотров, список НЕ должен сместиться на 1 элемент");
        System.out.println(manager.getHistoryManager().getHistory());
        System.out.println("Получаем подзадачу по id=22)");
        System.out.println(manager.getSubtaskById(22));
        System.out.println("Проверяем историю просмотров, null не должен добавляться в список");
        System.out.println(manager.getHistoryManager().getHistory());
        System.out.println("Удаляем подзадачу по id=21");
        System.out.println("Результат удаления подзадачу с id=21: " + manager.deleteSubtaskById(21));
        System.out.println("Проверяем историю просмотров, список НЕ должен содержать подзадачу с id=21");
        System.out.println(manager.getHistoryManager().getHistory());
        System.out.println("Удаляем эпик по id=14");
        System.out.println("Результат удаления эпика с id=14: " + manager.deleteEpicById(14));
        System.out.println("Проверяем историю просмотров, список НЕ должен содержать эпик с id=14 и все подзадачи этого эпика с id=16,17,18");
        System.out.println(manager.getHistoryManager().getHistory());
        System.out.println("_________________________________________________________________________________");
//_______________________________продолжение проверки работы истории___________________________________________
        System.out.println("Создаем задачу для проверки истории медеждера2");
        Task task10 = new Task("Задача1", "Проверка сохранения истории задачи1", TaskStatus.NEW);
        System.out.println(manager2.createTask(task10));
        System.out.println("Получаем подзадачу по id=0)");
        System.out.println(manager2.getTaskById(0));
        Task task11 = new Task("Задача2", "Проверка сохранения истории задачи2", TaskStatus.NEW);
        System.out.println(manager2.createTask(task11));
        System.out.println("Получаем подзадачу по id=1)");
        System.out.println(manager2.getTaskById(1));
        Task task12 = new Task("Задача3", "Проверка сохранения истории задачи3", TaskStatus.NEW);
        System.out.println(manager2.createTask(task12));
        System.out.println("Получаем подзадачу по id=2)");
        System.out.println(manager2.getTaskById(2));
        System.out.println("Проверяем историю просмотров");
        System.out.println(manager2.getHistoryManager().getHistory());
        System.out.println("Получаем подзадачу по id=0)");
        System.out.println(manager2.getTaskById(0));
        System.out.println("Проверяем историю просмотров, элемент с id=0 должен переместиться в конец списка");
        System.out.println(manager2.getHistoryManager().getHistory());
        System.out.println("Получаем подзадачу по id=1)");
        System.out.println(manager2.getTaskById(1));
        System.out.println("Проверяем историю просмотров, элемент с id=1 должен переместиться в конец списка");
        System.out.println(manager2.getHistoryManager().getHistory());
    }
}
