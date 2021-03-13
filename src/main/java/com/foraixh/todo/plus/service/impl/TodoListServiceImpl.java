package com.foraixh.todo.plus.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.foraixh.todo.plus.Repository.TodoTaskRepository;
import com.foraixh.todo.plus.annotation.UserLock;
import com.foraixh.todo.plus.configuration.GraphServiceClientFactory;
import com.foraixh.todo.plus.constant.TodoTaskTableConstants;
import com.foraixh.todo.plus.service.TodoListService;
import com.foraixh.todo.plus.service.TokenService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.models.extensions.TodoTask;
import com.microsoft.graph.models.extensions.TodoTaskList;
import com.microsoft.graph.requests.extensions.ITodoTaskCollectionPage;
import com.microsoft.graph.requests.extensions.ITodoTaskListCollectionPage;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    private final TokenService tokenService;
    private final TodoTaskRepository todoTaskRepository;

    public TodoListServiceImpl(TokenService tokenService, TodoTaskRepository todoTaskRepository) {
        this.tokenService = tokenService;
        this.todoTaskRepository = todoTaskRepository;
    }

    @Override
    public List<JsonObject> myTodoList(String userName) {
        IGraphServiceClient iGraphServiceClient = graphServiceClientFactory.getClient(tokenService.getTokenByUserName(userName));

        ITodoTaskListCollectionPage todoList = iGraphServiceClient.me().todo().lists().buildRequest().get();

        List<TodoTaskList> todoTaskList = todoList.getCurrentPage();

        return todoTaskList.stream()
                .map(TodoTaskList::getRawObject)
                .peek(jsonObject -> jsonObject.remove("@odata.etag"))
                .peek(jsonObject -> jsonObject.addProperty("userName", userName))
                .collect(Collectors.toList());
    }

    @Override
    public List<JsonObject> myTodoTask(String userName, String todoTaskListId) {
        IGraphServiceClient iGraphServiceClient =
                graphServiceClientFactory.getClient(tokenService.getTokenByUserName(userName));

        ITodoTaskCollectionPage todoTaskCollectionPage =
                iGraphServiceClient.me().todo().lists().byId(todoTaskListId).tasks().buildRequest().get();

        List<TodoTask> todoTaskList = todoTaskCollectionPage.getCurrentPage();

        return todoTaskList.stream()
                .map(TodoTask::getRawObject)
                .peek(jsonObject -> jsonObject.remove("@odata.etag"))
                .peek(jsonObject -> jsonObject.addProperty("userName", userName))
                .peek(jsonObject -> jsonObject.addProperty("todoTaskListId", todoTaskListId))
                .collect(Collectors.toList());
    }

//    @Transactional(rollbackFor = Exception.class)
    @UserLock
    @Override
    public void simpleSyncTodo(String userName) {
        List<JsonObject> todoTaskLists = myTodoList(userName);
        List<JsonObject> todoTasks = new LinkedList<>();
        todoTaskLists.forEach(item -> todoTasks.addAll(myTodoTask(userName, item.get("id").getAsString())));

        todoTaskRepository.syncTodoTaskList(userName, todoTaskLists);
        todoTaskRepository.syncTodoTask(userName, todoTasks);

        log.info("成功同步{}的todoTask；任务列表{}个；任务{}个", userName, todoTaskLists.size(), todoTasks.size());
    }
}
