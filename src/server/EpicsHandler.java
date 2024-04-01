package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import tasks.Epic;
import tasks.Subtask;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class EpicsHandler extends TasksHandler implements HttpHandler {

    public EpicsHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String method = httpExchange.getRequestMethod();
            switch (method) {
                case "GET": {
                    if (Pattern.matches("^/epics$", path)) {
                        String response = gson.toJson(taskManager.getEpics());
                        sendText(httpExchange, response);
                        break;
                    }
                    if (Pattern.matches("^/epics/\\d+$", path)) {
                        String pathId = path.replaceFirst("/epics/", "");
                        int id = parsePathId(pathId);
                        if (id != -1 && taskManager.getEpicById(id) != null) {
                            Epic epic = taskManager.findEpic(taskManager.getEpicById(id));
                            String response = gson.toJson(epic);
                            sendText(httpExchange, response);
                        } else {
                            System.out.println("Не найден эпик с id - " + id);
                            httpExchange.sendResponseHeaders(404, 0);
                            break;
                        }
                    }
                    if (Pattern.matches("^/epics/\\d+/subtasks$", path)) {
                        String pathId = path.replaceFirst("/epics/", "")
                                .replaceFirst("/subtasks", "");
                        int id = parsePathId(pathId);
                        if (id != -1 && taskManager.getEpicById(id) != null) {
                            Epic epic = taskManager.getEpicById(id);
                            List<Subtask> subtasks = epic.getListSubtask();
                            String response = gson.toJson(subtasks);
                            sendText(httpExchange, response);
                            break;
                        } else {
                            System.out.println("Не найден эпик с id - " + id);
                            httpExchange.sendResponseHeaders(404, 0);
                            break;
                        }
                    }
                    break;
                }
                case "POST": {
                    if (Pattern.matches("^/epics$", path)) {
                        Epic epic = gson.fromJson(readText(httpExchange), Epic.class);
                        taskManager.createEpic(epic);
                        System.out.println("Эпик успешно добавлен");
                        httpExchange.sendResponseHeaders(201, 0);
                        break;
                    } else {
                        System.out.println("Ожидается /epics запрос, а получили - " + path);
                        httpExchange.sendResponseHeaders(405, 0);
                    }
                    break;
                }
                case "DELETE": {
                    if (Pattern.matches("^/epics/\\d+$", path)) {
                        String pathId = path.replaceFirst("/epics/", "");
                        int id = parsePathId(pathId);
                        if (id != -1 && taskManager.getEpicById(id) != null) {
                            taskManager.removeEpic(taskManager.getEpicById(id));
                            System.out.println("Удален эпик с id - " + id);
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            System.out.println("Не найден эпик с id - " + id);
                            httpExchange.sendResponseHeaders(404, 0);
                        }

                    } else {
                        System.out.println("Ожидается /epics/{id} запрос, а получили - " + path);
                        httpExchange.sendResponseHeaders(405, 0);
                    }
                    break;
                }
                default: {
                    System.out.println("Ожидается GET, POST или DELETE запрос, а получили - " + method);
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

