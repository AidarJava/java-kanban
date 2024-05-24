import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Integer> subtaskId = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(String name, String description, Integer id) {
        super(name, description, id);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", subtaskId=" + subtaskId +
                '}';
    }
}

