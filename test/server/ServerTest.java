package server;

import com.google.gson.Gson;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import tasks.Task;

import java.io.IOException;

public class ServerTest {
    TaskManager taskManager = Managers.getDefault();
   protected HttpTaskServer taskServer = new HttpTaskServer(taskManager);
    protected final Gson gson = HttpTaskServer.getGson();
   protected Task task;

    public void EpicServerTest() throws IOException {
    }

    public ServerTest() throws IOException {
    }

    @BeforeEach
    void setUp() throws IOException {
        taskManager.clearTasks();
        taskManager.clearSubtasks();
        taskManager.clearEpics();
        taskServer.start();
    }

    @AfterEach
    void tearDown() {
        taskServer.stop();
    }
}
