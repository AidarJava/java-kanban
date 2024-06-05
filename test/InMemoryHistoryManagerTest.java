import org.junit.jupiter.api.Test;
import ru.homework.taskmanager.enums.TaskStatus;
import ru.homework.taskmanager.model.Task;
import ru.homework.taskmanager.service.HistoryManager;
import ru.homework.taskmanager.service.InMemoryHistoryManager;
import ru.homework.taskmanager.service.Managers;
import ru.homework.taskmanager.service.TaskManager;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InMemoryHistoryManagerTest {

    @Test
    public void addToHistoryShouldBeEqualsWithItInitial(){
        TaskManager manager = Managers.getDefault();
        HistoryManager managerHis = Managers.getDefaultHistory();
        Task task1 = new Task("Задача1", "Проверка создания1", TaskStatus.IN_PROGRESS);
        manager.createTask(task1);
        manager.getTaskById(0);
        final ArrayList<Task> history = managerHis.getHistory();
        assertNotNull(history, "История пустая.");
        assertEquals(1, history.size(), "История пустая.");
        assertEquals(task1.getName(),history.getFirst().getName(),"Поле name не совпадает" );
        assertEquals(task1.getDescription(),history.getFirst().getDescription(), "Поле description не совпадает");
        assertEquals(task1.getStatus(),history.getFirst().getStatus(), "Статус не совпадает");
    }

}