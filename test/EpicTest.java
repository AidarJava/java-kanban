import org.junit.jupiter.api.Test;
import ru.homework.taskmanager.model.Epic;
import ru.homework.taskmanager.model.Task;
import ru.homework.taskmanager.service.Managers;
import ru.homework.taskmanager.service.TaskManager;

import java.util.ArrayList;

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

        final ArrayList<Epic> epics = taskManager.getEpics();

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
}