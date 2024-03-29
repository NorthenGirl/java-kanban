package managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import server.DurationAdapter;
import server.LocalDateTimeAdapter;

import java.io.File;

public class Managers {
    public static final File FILE = new File("tasks.csv");

    public static TaskManager getDefault() {
        return new FileBackedTaskManager(FILE);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
