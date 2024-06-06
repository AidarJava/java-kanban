import org.junit.jupiter.api.Test;
import ru.homework.taskmanager.enums.TaskStatus;
import ru.homework.taskmanager.model.Epic;
import ru.homework.taskmanager.model.Subtask;
import ru.homework.taskmanager.model.Task;
import ru.homework.taskmanager.service.InMemoryHistoryManager;
import ru.homework.taskmanager.service.InMemoryTaskManager;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    @Test
    public void shouldInMemoryTaskManagerCanCreateAndFindForId() {
        InMemoryTaskManager manager = new InMemoryTaskManager(new InMemoryHistoryManager());
        Task task1 = new Task("Задача1", "Проверка создания1", TaskStatus.NEW);
        manager.createTask(task1);
        assertNotNull(manager.getTasks(), "Не добавил задачу");
        assertNotNull(manager.getTaskById(0), "Не нашел задачу по id");
        Epic epic1 = new Epic("Эпик1", "Проверка создания1");
        manager.createEpic(epic1);
        assertNotNull(manager.getEpics(), "Не добавил эпик");
        assertNotNull(manager.getEpicById(1), "Не нашел эпик по id");
        Subtask sub1 = new Subtask("Подзадача1", "Проверка создания1", TaskStatus.NEW, 1);
        manager.createSubtask(sub1);
        assertNotNull(manager.getSubtascs(), "Не добавил подзадачу");
        assertNotNull(manager.getSubtaskById(2), "Не нашел подзадачу по id");
    }

    @Test
    public void shouldBeNoConflictWithGenAndCreate() {
        InMemoryTaskManager manager = new InMemoryTaskManager(new InMemoryHistoryManager());
        Task task1 = new Task("Задача1", "Проверка конфликта1", TaskStatus.NEW);
        manager.createTask(task1);
        Task task2 = new Task("Задача2", "Проверка конфликта2", 0, TaskStatus.IN_PROGRESS);
        manager.getTasks().add(task2);
        assertEquals(1, manager.getTasks().size(), "Добавилась");
        manager.createTask(task2);
        assertEquals(2, manager.getTasks().size(), "Не добавилась");
        ArrayList<Task> tasks = manager.getTasks();
        assertNotEquals(tasks.getFirst().getId(), tasks.getLast().getId(), "Конфликт");
    }

    @Test
    public void addToManagerShouldBeEqualsWithItInitial() {
        InMemoryTaskManager manager = new InMemoryTaskManager(new InMemoryHistoryManager());
        Task task1 = new Task("Задача1", "Проверка создания1", TaskStatus.NEW);
        manager.createTask(task1);
        assertEquals(task1.getName(), manager.getTaskById(0).getName());
        assertEquals(task1.getDescription(), manager.getTaskById(0).getDescription());
        assertEquals(task1.getStatus(), manager.getTaskById(0).getStatus());
    }
}
