import org.junit.jupiter.api.Test;
import ru.homework.taskmanager.enums.TaskStatus;
import ru.homework.taskmanager.model.Epic;
import ru.homework.taskmanager.model.Subtask;
import ru.homework.taskmanager.model.Task;
import ru.homework.taskmanager.service.Managers;
import ru.homework.taskmanager.service.TaskManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SubtaskTest {
    @Test
    void addNewSubtask() {
        TaskManager taskManager = Managers.getDefault();
        Epic epic = new Epic("Эпик", "Проверка создания");
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Подзадача", "Проверка создания", TaskStatus.NEW, taskManager.createEpic(epic).getId());
        final int subtaskId = taskManager.createSubtask(subtask).getId();
        final Task savedSubtask = taskManager.getSubtaskById(subtaskId);
        assertNotNull(savedSubtask, "Подзадача не найдена.");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают.");

        final List<Subtask> subtasks = taskManager.getSubtascs();

        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask, subtasks.get(0), "Подзадачи не совпадают.");
    }
    @Test
    public void subtasksShouldBeEqualsIfIdIsEqual(){
        Task sub1 = new Subtask("Подзадача1", "Проверка равенства1", 2, TaskStatus.NEW, 5);
        Task sub2 = new Subtask("Подзадача2", "Проверка равенства2",2, TaskStatus.DONE, 5);
        assertEquals(sub1,sub2,"Подзадачи не равны.");
    }
}