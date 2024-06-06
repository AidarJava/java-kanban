import ru.homework.taskmanager.enums.TaskStatus;
import ru.homework.taskmanager.model.Task;
import ru.homework.taskmanager.service.*;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {
    @Test
    void addNewTask() {
        TaskManager taskManager = Managers.getDefault();
        Task task = new Task("Задача", "Проверка создания", TaskStatus.NEW);
        final int taskId = taskManager.createTask(task).getId();
        final Task savedTask = taskManager.getTaskById(taskId);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }
    @Test
    public void tasksShouldBeEqualsIfIdIsEqual(){
        Task task1 = new Task("Задача1", "Проверка равенства1",1, TaskStatus.NEW);
        Task task2 = new Task("Задача2", "Проверка равенства2",1, TaskStatus.IN_PROGRESS);
        assertEquals(task1,task2, "Задачи не равны.");
    }
}