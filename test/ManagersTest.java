import org.junit.jupiter.api.Test;
import ru.homework.taskmanager.service.Managers;
import ru.homework.taskmanager.service.TaskManager;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ManagersTest {
    @Test
    public void shouldManagersReturnInitializedManagers() {
        TaskManager manager = Managers.getDefault();
        assertNotNull(manager);
    }
}