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

    /**
     * 同步${userName}的todo任务列表和对应的任务
     * 物理删除，不使用逻辑删除
     * @param userName userName
     */
    @Transactional(rollbackFor = Exception.class)
    @UserLock(paramIndex = 0)
    @Deprecated
    public void syncTodo(String userName) {
        // 数据库中todoTaskList数据
        List<JsonObject> elementList = todoTaskRepository.selectAllTodoTaskListByUserName(userName);
        Map<String, JsonObject> elementMap = elementList.stream().collect(Collectors.toMap(
                (JsonObject jsonObject) -> jsonObject.get("id").getAsString(),
                jsonObject -> jsonObject
        ));

        // microsoft todo最新数据
        List<JsonObject> latestList = myTodoList(userName);
        Map<String, JsonObject> latestMap = latestList.stream().collect(Collectors.toMap(
                (JsonObject jsonObject) -> jsonObject.get("id").getAsString(),
                jsonObject -> jsonObject
        ));

        List<JsonObject> needToInsertList = latestList.stream()
                .filter(jsonObject -> !elementMap.containsKey(jsonObject.get("id").getAsString()))
                .collect(Collectors.toList());
        insertTodoTaskListAndTodoTask(userName, needToInsertList);

        List<String> needToDeleteList = elementList.stream()
                .map((JsonObject jsonObject) -> jsonObject.get("id").getAsString())
                .filter(id -> !latestMap.containsKey(id))
                .collect(Collectors.toList());
        deleteTodoTaskListAndTodoTask(userName, needToDeleteList);

        // 需要覆盖的todoTaskList
        List<JsonObject> needToReplaceList = latestList.stream()
                .filter(jsonObject -> elementMap.containsKey(jsonObject.get("id").getAsString()))
                .collect(Collectors.toList());
        replaceTodoTaskListAndTodoTask(userName, needToReplaceList);
    }

    /**
     * 更新todoTaskList的列表和todoTaskList列表下的任务
     * @param userName userName
     * @param jsonObjectList todoTaskList的列表
     */
    @Deprecated
    private void replaceTodoTaskListAndTodoTask(String userName, List<JsonObject> jsonObjectList) {
        List<String> idList = jsonObjectList.stream()
                .map((JsonObject jsonObject) -> jsonObject.get("id").getAsString())
                .collect(Collectors.toList());

        todoTaskRepository.deleteById(idList, TodoTaskTableConstants.TODO_TASK_LIST_TABLE);
        todoTaskRepository.insert(jsonObjectList, TodoTaskTableConstants.TODO_TASK_LIST_TABLE);

        for (JsonObject todoTaskList : jsonObjectList) {
            String todoTaskListId = todoTaskList.get("todoTaskListId").getAsString();
            // 数据库中todoTask数据
            List<JsonObject> elementList =
                    todoTaskRepository.selectAllTodoTaskByUserNameAndTodoTaskListId(userName, todoTaskListId);
            Map<String, JsonObject> elementMap = elementList.stream().collect(Collectors.toMap(
                    (JsonObject jsonObject) -> jsonObject.get("id").getAsString(),
                    jsonObject -> jsonObject
            ));

            // microsoft todo最新数据
            List<JsonObject> latestList = myTodoTask(userName, todoTaskListId);
            Map<String, JsonObject> latestMap = latestList.stream().collect(Collectors.toMap(
                    (JsonObject jsonObject) -> jsonObject.get("id").getAsString(),
                    jsonObject -> jsonObject
            ));
        }
    }

    /**
     * 插入todoTaskList的列表和todoTaskList列表下的任务
     * @param userName userName
     * @param jsonObjectList todoTaskList的列表
     */
    @Deprecated
    private void insertTodoTaskListAndTodoTask(String userName, List<JsonObject> jsonObjectList) {
        List<JsonObject> todoTasks = new LinkedList<>();

        for (JsonObject jsonObject : jsonObjectList) {
            todoTasks.addAll(myTodoTask(userName, jsonObject.get("id").getAsString()));
        }

        todoTaskRepository.insert(jsonObjectList, TodoTaskTableConstants.TODO_TASK_LIST_TABLE);
        todoTaskRepository.insert(todoTasks, TodoTaskTableConstants.TODO_TASK_TABLE);
    }

    /**
     * 删除todoTaskList的列表和todoTaskList列表下的任务
     * @param userName userName
     * @param idList todoTaskList的id的列表
     */
    @Deprecated
    private void deleteTodoTaskListAndTodoTask(String userName, List<String> idList) {
        todoTaskRepository.deleteById(idList, TodoTaskTableConstants.TODO_TASK_LIST_TABLE);
        todoTaskRepository.deleteByTodoTaskListId(idList, TodoTaskTableConstants.TODO_TASK_TABLE);
    }
}
