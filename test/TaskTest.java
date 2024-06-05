package ru.homework.taskmanager.tests;

import ru.homework.taskmanager.enums.TaskStatus;
import ru.homework.taskmanager.model.Epic;
import ru.homework.taskmanager.model.Subtask;
import ru.homework.taskmanager.model.Task;
import ru.homework.taskmanager.service.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {
    @Test
    public void tasksShouldBeEqualsIfIdIsEqual(){
        //TaskManager manager = Managers.getDefault();
        Task task1 = new Task("Задача1", "Проверка создания1",1, TaskStatus.NEW);
        Task task2 = new Task("Задача2", "Проверка создания2",1, TaskStatus.IN_PROGRESS);
        assertEquals(task1,task2);
    }

    @Test
    public void epicsShouldBeEqualsIfIdIsEqual(){
        //TaskManager manager = Managers.getDefault();
        //Task sub1 = new Subtask("Подзадача1", "Проверка создания1", 5,TaskStatus.NEW, 5);
        Epic epic1 = new Epic("Эпик5", "Проверка проверка работы с подзадачами5",5);
        Epic epic2 = new Epic("Эпик4", "Проверка проверка работы с подзадачами1",5);
        assertEquals(epic1,epic2,"не прошло");
    }
    @Test
    public void subtasksShouldBeEqualsIfIdIsEqual(){
        //TaskManager manager = Managers.getDefault();
        //Task sub1 = new Subtask("Подзадача1", "Проверка создания1", 5,TaskStatus.NEW, 5);
        Subtask sub1 = new Subtask("Подзадача1", "Проверка создания1", 2,TaskStatus.NEW, 5);
        Subtask sub2 = new Subtask("Подзадача2", "Проверка создания2",2, TaskStatus.DONE, 5);
        assertEquals(sub1,sub2,"не прошло");
    }
    @Test
    public void shouldManagersReturnInitializedManagers(){
        TaskManager manager = Managers.getDefault();
        assertNotNull(manager);
    }

    @Test
    public void shouldInMemoryTaskManagerCanCreateAndFindForId(){
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Task task1 = new Task("Задача1", "Проверка создания1", TaskStatus.NEW);
        manager.createTask(task1);
        assertNotNull(manager.getTaskById(0),"не прошло");
        Epic epic1 = new Epic("Эпик1", "Проверка создания1");
        manager.createEpic(epic1);
        assertNotNull(manager.getEpicById(1),"не прошло");
        Subtask sub1 = new Subtask("Подзадача1", "Проверка создания1", TaskStatus.NEW, 1);
        manager.createSubtask(sub1);
        assertNotNull(manager.getSubtaskById(2),"не прошло");
    }
    @Test
    public void shouldBeNoConflictWithGenAndCreate(){
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Task task1 = new Task("Задача1", "Проверка конфликта1", 0,TaskStatus.NEW);
        Task task2 = new Task("Задача2", "Проверка конфликта2",TaskStatus.IN_PROGRESS);
        manager.createTask(task2);
        assertEquals(task1,task2);
    }
    @Test
    public void addToManagerShouldBeEqualsWithItInitial(){
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Task task1 = new Task("Задача1", "Проверка создания1",TaskStatus.NEW);
        manager.createTask(task1);
        assertEquals(task1.getName(),manager.getTaskById(0).getName());
        assertEquals(task1.getDescription(),manager.getTaskById(0).getDescription());
        assertEquals(task1.getStatus(),manager.getTaskById(0).getStatus());
    }
    @Test
    public void addToHistoryShouldBeEqualsWithItInitial(){
        TaskManager manager = Managers.getDefault();
        HistoryManager managerHis = Managers.getDefaultHistory();
        Task task1 = new Task("Задача1", "Проверка создания1",TaskStatus.NEW);
        manager.createTask(task1);
        manager.getTaskById(0);
        ArrayList<Task> history = managerHis.getHistory();
        assertEquals(task1.getName(),history.getFirst().getName());
        assertEquals(task1.getDescription(),history.getFirst().getDescription());
        assertEquals(task1.getStatus(),history.getFirst().getStatus());
    }

}