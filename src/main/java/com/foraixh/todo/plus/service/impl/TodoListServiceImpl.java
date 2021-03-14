package com.foraixh.todo.plus.service.impl;

import com.foraixh.todo.plus.Repository.TodoTaskRepository;
import com.foraixh.todo.plus.annotation.UserLock;
import com.foraixh.todo.plus.configuration.GraphServiceClientFactory;
import com.foraixh.todo.plus.pojo.TodoPlusTask;
import com.foraixh.todo.plus.pojo.TodoPlusTaskList;
import com.foraixh.todo.plus.service.TodoListService;
import com.foraixh.todo.plus.service.TodoPlusConverterService;
import com.foraixh.todo.plus.service.TokenService;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

    private final Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {

        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            //过滤掉字段名包含"age"
            return f.getName().contains("rawObject") || f.getName().contains("serializer");
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }).create();

    private final TokenService tokenService;
    private final TodoTaskRepository todoTaskRepository;
    private final TodoPlusConverterService converterService;

    public TodoListServiceImpl(TokenService tokenService, TodoTaskRepository todoTaskRepository,
                               TodoPlusConverterService converterService) {
        this.tokenService = tokenService;
        this.todoTaskRepository = todoTaskRepository;
        this.converterService = converterService;
    }

    @Override
    public List<TodoPlusTaskList> myTodoList(String userName) {
        IGraphServiceClient iGraphServiceClient = graphServiceClientFactory.getClient(tokenService.getTokenByUserName(userName));

        ITodoTaskListCollectionPage todoList = iGraphServiceClient.me().todo().lists().buildRequest().get();

        List<TodoTaskList> todoTaskList = todoList.getCurrentPage();

        return todoTaskList.stream()
                .map(item -> converterService.convertTodoToTodoPlusTaskList(userName, item))
                .collect(Collectors.toList());
    }

    @Override
    public List<TodoPlusTask> myTodoTask(String userName, String todoTaskListId) {
        IGraphServiceClient iGraphServiceClient =
                graphServiceClientFactory.getClient(tokenService.getTokenByUserName(userName));

        ITodoTaskCollectionPage todoTaskCollectionPage =
                iGraphServiceClient.me().todo().lists().byId(todoTaskListId).tasks().buildRequest().get();

        List<TodoTask> todoTaskList = todoTaskCollectionPage.getCurrentPage();

        return todoTaskList.stream()
                .map(item -> converterService.convertTodoToTodoPlusTask(userName, item, todoTaskListId))
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @UserLock
    @Override
    public void simpleSyncTodo(String userName) {
        log.info("开始同步{}的TodoTask", userName);
        List<TodoPlusTaskList> todoTaskLists = myTodoList(userName);
        List<TodoPlusTask> todoTasks = new LinkedList<>();
        todoTaskLists.forEach(item -> todoTasks.addAll(myTodoTask(userName, item.todoPlusId)));

        todoTaskRepository.syncTodoTaskList(userName, todoTaskLists);
        todoTaskRepository.syncTodoTask(userName, todoTasks);

        log.info("成功同步{}的todoTask；任务列表{}个；任务{}个", userName, todoTaskLists.size(), todoTasks.size());
    }
}
