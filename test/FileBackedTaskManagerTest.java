import org.junit.jupiter.api.Test;
import ru.homework.taskmanager.enums.TaskStatus;
import ru.homework.taskmanager.model.Epic;
import ru.homework.taskmanager.model.Subtask;
import ru.homework.taskmanager.model.Task;
import ru.homework.taskmanager.service.FileBackedTaskManager;
import ru.homework.taskmanager.service.Managers;
import ru.homework.taskmanager.service.TaskManager;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.homework.taskmanager.service.FileBackedTaskManager.loadFromFile;

public class FileBackedTaskManagerTest {

    @Test
    public void loadObjectsFromFile() throws IOException {
        File myfile = new File(System.getProperty("user.dir"));
        File tempFile = File.createTempFile("test", ".csv", myfile);
        tempFile.deleteOnExit();
        TaskManager taskManager = Managers.getDefault();
        TaskManager manager = new FileBackedTaskManager(taskManager.getHistoryManager(), tempFile);
        System.out.println("Создаем задачу");
        Task task1 = new Task("Task1", "Checking create1", TaskStatus.NEW);
        System.out.println(manager.createTask(task1));
        System.out.println("Создаем эпик");
        Epic epic1 = new Epic("Epic1", "Checking create1");
        System.out.println(manager.createEpic(epic1));
        System.out.println("Создаем подзадачи");
        Subtask sub1 = new Subtask("Subtask1", "Checking create1", TaskStatus.NEW, 1);
        System.out.println(manager.createSubtask(sub1));
        TaskManager manager2 = loadFromFile(tempFile);
        assertEquals(manager.getTasks().size(), manager2.getTasks().size(), "Не все задачи загрузились.");
        assertEquals(manager.getEpics().size(), manager2.getEpics().size(), "Не все эпики загрузились.");
        assertEquals(manager.getSubtascs().size(), manager2.getSubtascs().size(), "Не все подзадачи загрузились.");
        assertEquals(manager.getTasks().getFirst(), manager2.getTasks().getFirst(), "Задачи не совпадают.");
        assertEquals(manager.getEpics().getFirst(), manager2.getEpics().getFirst(), "Эпики не совпадают.");
        assertEquals(manager.getSubtascs().getFirst(), manager2.getSubtascs().getFirst(), "Подзадачи не совпадают.");
    }
}



