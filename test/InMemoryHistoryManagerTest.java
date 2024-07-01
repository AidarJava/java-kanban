import org.junit.jupiter.api.Test;
import ru.homework.taskmanager.enums.TaskStatus;
import ru.homework.taskmanager.model.Task;
import ru.homework.taskmanager.service.HistoryManager;
import ru.homework.taskmanager.service.InMemoryHistoryManager;
import ru.homework.taskmanager.service.Managers;
import ru.homework.taskmanager.service.TaskManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    @Test
    public void addToHistoryShouldBeEqualsWithItInitial(){
        TaskManager manager = Managers.getDefault();
        Task task1 = new Task("Задача1", "Проверка создания1", TaskStatus.IN_PROGRESS);
        manager.createTask(task1);
        manager.getTaskById(0);
        final List<Task> history = manager.getHistoryManager().getHistory();
        assertNotNull(history, "История пустая.");
        assertEquals(1, history.size(), "История пустая.");
        assertEquals(task1.getName(),history.getFirst().getName(),"Поле name не совпадает" );
        assertEquals(task1.getDescription(),history.getFirst().getDescription(), "Поле description не совпадает");
        assertEquals(task1.getStatus(),history.getFirst().getStatus(), "Статус не совпадает");
    }
    @Test
    public void addToHistoryEqualIdTasksShouldNotDoublingItInHistory(){
        TaskManager manager = Managers.getDefault();
        Task task1 = new Task("Задача1", "Проверка создания1", TaskStatus.IN_PROGRESS);
        manager.createTask(task1);
        manager.getTaskById(0);
        manager.getTaskById(0);
        final List<Task> history = manager.getHistoryManager().getHistory();
        assertEquals(1, history.size(), "Не удалилась повторяющая задача.");
    }
    @Test
    public void addToHistoryTasksShouldPutItToEndOfHistory(){
        TaskManager manager = Managers.getDefault();
        Task task1 = new Task("Задача1", "Проверка создания1", TaskStatus.IN_PROGRESS);
        manager.createTask(task1);
        Task task2 = new Task("Задача2", "Проверка создания2", TaskStatus.IN_PROGRESS);
        manager.createTask(task2);
        Task task3 = new Task("Задача3", "Проверка создания3", TaskStatus.IN_PROGRESS);
        manager.createTask(task3);
        manager.getTaskById(0);
        manager.getTaskById(1);
        manager.getTaskById(2);
        manager.getTaskById(0);
        final List<Task> history = manager.getHistoryManager().getHistory();
        assertEquals(history.getFirst(), task2, "Не сработало перемещение в конец списка");
        assertEquals(history.getLast(), task1, "Не сработало перемещение в конец списка");

    }
    @Test
    public void removeTaskShouldRemoveFromHistory(){
        TaskManager manager = Managers.getDefault();
        Task task1 = new Task("Задача1", "Проверка создания1", TaskStatus.IN_PROGRESS);
        manager.createTask(task1);
        manager.getTaskById(0);
        manager.deleteTaskById(0);
        final List<Task> history = manager.getHistoryManager().getHistory();
        assertEquals(0, history.size(), "Не удалилась задача из истории, при полном удалеении задачи.");
    }

}