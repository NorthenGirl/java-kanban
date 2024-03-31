package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;

import java.io.IOException;
import java.util.regex.Pattern;

public class HistoryHandler extends TasksHandler implements HttpHandler {
    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String method = httpExchange.getRequestMethod();
            switch (method) {
                case "GET": {
                    if (Pattern.matches("^/history$", path)) {
                        String response = gson.toJson(taskManager.getHistory());
                        sendText(httpExchange, response);
                        break;
                    } else {
                        System.out.println("Ожидается /history запрос, а получили - " + path);
                        httpExchange.sendResponseHeaders(405, 0);
                        break;
                    }
                }
                default: {
                    System.out.println("Ожидается GET запрос, а получили - " + method);
                    httpExchange.sendResponseHeaders(405, 0);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();

        } finally {
            httpExchange.close();
        }
    }
}
