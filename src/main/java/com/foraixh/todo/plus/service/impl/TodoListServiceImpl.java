package com.foraixh.todo.plus.service.impl;

import com.foraixh.todo.plus.component.RedisDelayedQueue;
import com.foraixh.todo.plus.configuration.GraphServiceClientFactory;
import com.foraixh.todo.plus.constant.MicrosoftGraphConstants;
import com.foraixh.todo.plus.service.TodoListService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.models.extensions.TodoTask;
import com.microsoft.graph.models.extensions.TodoTaskList;
import com.microsoft.graph.requests.extensions.ITodoTaskCollectionPage;
import com.microsoft.graph.requests.extensions.ITodoTaskListCollectionPage;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    private final RedissonClient redissonClient;
    private final RedisDelayedQueue redisDelayedQueue;

    public TodoListServiceImpl(RedissonClient redissonClient, RedisDelayedQueue redisDelayedQueue) {
        this.redissonClient = redissonClient;
        this.redisDelayedQueue = redisDelayedQueue;
    }

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

    @Override
    public void tokenStorageScheduleRefresh(IAuthenticationResult result) {
        JsonObject authResult = gson.fromJson(gson.toJson(result), JsonObject.class);

        RMap<String, Object> tokenMap = redissonClient.getMap(result.account().username());
        tokenMap.expire(Instant.ofEpochSecond(authResult.get(MicrosoftGraphConstants.EXPIRES_ON).getAsLong()));
        tokenMap.put(MicrosoftGraphConstants.ACCESS_TOKEN, result.accessToken());
        tokenMap.put(MicrosoftGraphConstants.REFRESH_TOKEN, authResult.get(MicrosoftGraphConstants.REFRESH_TOKEN).getAsString());

        redisDelayedQueue.addQueue(
                result.account().username(),
                authResult.get(MicrosoftGraphConstants.EXPIRES_ON).getAsLong() - Instant.now().getEpochSecond() - 1000 * 10,
                TimeUnit.SECONDS,
                MicrosoftGraphConstants.TOKEN_REDIS_SCHEDULE_REFRESH_QUEUE
        );
    }
}
