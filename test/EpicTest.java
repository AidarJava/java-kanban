import org.junit.jupiter.api.Test;
import ru.homework.taskmanager.model.Epic;
import ru.homework.taskmanager.model.Subtask;
import ru.homework.taskmanager.model.Task;
import ru.homework.taskmanager.enums.TaskStatus;
import ru.homework.taskmanager.service.Managers;
import ru.homework.taskmanager.service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    @Test
    void addNewEpic() {
        TaskManager taskManager = Managers.getDefault();
        Epic epic = new Epic("Эпик", "Проверка создания");
        final int epicId = taskManager.createEpic(epic).getId();
        final Task savedEpic = taskManager.getEpicById(epicId);
        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");

        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    public void epicsShouldBeEqualsIfIdIsEqual() {
        Task epic1 = new Epic("Эпик1", "Проверка равенства1", 5);
        Task epic2 = new Epic("Эпик2", "Проверка равенства2", 5);
        assertEquals(epic1, epic2, "Эпики не равны.");
    }
// проверьте, что объект Epic нельзя добавить в самого себя в виде подзадачи (не совсем понятен смысл теста,
// невозможно создать подзадачу без отсутсвующего у эпика параметра - int epicId, аналогично для Subtask нельзя
// сделать своим же эпиком)
//    @Test
//    public void shouldNotAddEpicToHimselfForSubtask(){
//        TaskManager manager = Managers.getDefault();
//        Task epic1 = new Epic("Эпик1", "Проверка равенства1");
//        manager.createEpic((Epic) epic1);
//        manager.createSubtask((Subtask) epic1);
//        assertNull(manager.getSubtasks(),"Не создается подзадача");
//    }
@Test
void statusEpicIsDoneOrInProgress() {
    TaskManager taskManager = Managers.getDefault();
    Epic epic = new Epic("Эпик", "Проверка создания");
    taskManager.createEpic(epic);
    Subtask sub1 = new Subtask("Подзадача1", "Проверка создания1", TaskStatus.DONE, epic.getId(), Duration.ofMinutes(5),
            LocalDateTime.of(2024, 2, 3, 3, 44));
    taskManager.createSubtask(sub1);
    Subtask sub2 = new Subtask("Подзадача2", "Проверка создания2", TaskStatus.DONE, epic.getId(),Duration.ofMinutes(1),
            LocalDateTime.of(2024, 2, 3, 3, 30));
    taskManager.createSubtask(sub2);
    Subtask sub3 = new Subtask("Подзадача3", "Проверка создания3", TaskStatus.DONE, epic.getId(),Duration.ofMinutes(2),
            LocalDateTime.of(2024, 6, 5, 5, 35));
    taskManager.createSubtask(sub3);
    assertEquals(epic.status, TaskStatus.DONE, "Статусы не совпадают.");
    Subtask sub4 = new Subtask("Изм_подз1", "Проверка изменения", sub2.getId(), TaskStatus.NEW, epic.getId(),Duration.ofMinutes(1),
            LocalDateTime.of(2024, 2, 3, 3, 30));
    taskManager.updateSubtask(sub4);
    assertEquals(epic.status, TaskStatus.IN_PROGRESS, "Статусы не совпадают.");

}
}