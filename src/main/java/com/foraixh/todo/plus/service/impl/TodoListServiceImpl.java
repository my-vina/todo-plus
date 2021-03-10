package com.foraixh.todo.plus.service.impl;

import com.foraixh.todo.plus.configuration.GraphServiceClientFactory;
import com.foraixh.todo.plus.service.TodoListService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.models.extensions.TodoTask;
import com.microsoft.graph.models.extensions.TodoTaskList;
import com.microsoft.graph.requests.extensions.ITodoTaskCollectionPage;
import com.microsoft.graph.requests.extensions.ITodoTaskListCollectionPage;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author myvina
 * @date 2021/03/10 10:55
 * @usage todolist服务实现类
 */
@Service
public class TodoListServiceImpl implements TodoListService {
    private static final Logger log = getLogger(TodoListServiceImpl.class);

    private final GraphServiceClientFactory graphServiceClientFactory = new GraphServiceClientFactory();

    private final Gson gson = new Gson();

    @Override
    public JsonArray myTodoList(String token) {
        IGraphServiceClient iGraphServiceClient = graphServiceClientFactory.getClient(token);

        ITodoTaskListCollectionPage todoList = iGraphServiceClient.me().todo().lists().buildRequest().get();

        List<TodoTaskList> todoTaskList = todoList.getCurrentPage();

        JsonArray jsonArray = new JsonArray();
        todoTaskList.forEach(data -> jsonArray.add(data.getRawObject()));

        return jsonArray;
    }

    @Override
    public JsonArray myTodoTask(String token, String todoTaskListId) {
        IGraphServiceClient iGraphServiceClient = graphServiceClientFactory.getClient(token);

        ITodoTaskCollectionPage todoTaskCollectionPage =
                iGraphServiceClient.me().todo().lists().byId(todoTaskListId).tasks().buildRequest().get();

        List<TodoTask> todoTaskList = todoTaskCollectionPage.getCurrentPage();

        JsonArray jsonArray = new JsonArray();
        todoTaskList.forEach(data -> jsonArray.add(data.getRawObject()));

        return jsonArray;
    }
}
