package com.foraixh.todo.plus;

import com.foraixh.todo.plus.configuration.GraphServiceClientFactory;
import com.foraixh.todo.plus.response.GlobalResponse;
import com.foraixh.todo.plus.service.TodoListService;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.microsoft.aad.msal4j.*;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.requests.extensions.ITodoTaskListCollectionPage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.microsoft.graph.models.extensions.User;

import java.net.MalformedURLException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * @author myvina
 * @date 2021/02/22 16:25
 * @usage
 */
@RestController
@RequestMapping("/demo")
public class DemoController {
    /** Set authority to allow only organizational accounts
     *  Device code flow only supports organizational accounts
     */
    private final static String AUTHORITY = "https://login.microsoftonline.com/common/";

    @Value("${todo-plus.app.id}")
    private String appId;
    @Value("${todo-plus.app.scopes}")
    private String[] scopes;
    @Value("${todo-plus.demo.userName}")
    private String userName;
    @Value("${todo-plus.demo.password}")
    private String password;

    private final GraphServiceClientFactory graphServiceClientFactory = new GraphServiceClientFactory();

    private final TodoListService todoListService;

    public DemoController(TodoListService todoListService) {
        this.todoListService = todoListService;
    }

    @GetMapping("/todo/list/{todoListId}")
    public GlobalResponse<JsonArray> todoTaskList(String token, @PathVariable String todoListId) {
        return GlobalResponse.success(todoListService.myTodoTask(token, todoListId));
    }

    @GetMapping("/todo/list")
    public GlobalResponse<JsonArray> todoList(String token) {
        return GlobalResponse.success(todoListService.myTodoList(token));
    }

    @GetMapping("/welcomeMe")
    public Object welcomeMe() {
        String token = "EwB4A8l6BAAU6k7+XVQzkGyMv7VHB/h4cHbJYRAAAUQZq4Spu" +
                "+xuPpeaEeRgkSEWn0XjWZDwEyHHmhEXD30WdqSeAibYwspAewGvXEtQRdiwzxnU8G7igHgF8iWw3vaowmn5hJDngYSWLM2GWqudIvds13kAH9bmD1TNLC3jlFDXet6jsKYKHKMUZyat4YwoEjktVf+qsA95FTHJ8DpXyJb6nP1BQYGGCkoSTWP4+aVUTZsJ9Fn4gfnPXeYOMDW39lnnZM1ttHspjyLQoApw1TY+ztCCaRIrMHJtJJOiQjxk8gmdrF2GHhr+Env1abDu3GZYS8q3lKXKmWsw/JymA1rdxgnj3ODjeOOj2IQDbki+EWCLEOo78i47B5oGbWoDZgAACIJi4b4m9RnqSAIfDiKNHouqqWyOzzJ1TowByKQbWVzEsXDDOkaANFwe4OsYFJynmFtYvOugBCLgRVGTZ7cvjmoOGqtxEfmXjMXoBU/QpiZhwpQzwpIG1o+hg9BI+YblBcSypymGW5YzSDAC+98HaPkiAVXm1CMv4THs7L8a8uvc91ZI9ldPvO6fbsA7yMIzE3Z9SetZgBvdG0U5kQmshEKewL4cey6xNpbMvi8OeJ/XQ455UwdnkQRtDc/xDHmu7tNlGQ27aE+XEndzqDzS6dLYpJ1oWdjCRVgsJvIpIy2PniGq7zzjE0X417pu3Dp2sT9oi8boNfGcg1K7gfAioJYG12c8QPVQtpw342oY8uXdQoFxhov8ZKMHNS1+iGD0OSbUJefskkcRBB32SaYby6lYlwVIRZ108XBrFSVdEUGv+9hQMQJEuPzm/V5UCR+qfXzMPlv6uwWVcPrMm4nYMZIMGqIYnanJF6LaIXyW2kvMLxKVqLBvoPS+D+br85i7F4080FlGkbBWQgiyD6p2Cwi2CQCn6hOvY8qvRNCHsqVcm3f0OZR2DNYylo4ZdQHQSY5X+hPMd7fZYWWcTlIslRO2SO4K2BHPPC9supHJWF6EMR2/9ea8lvumJqmazaQw8lXkFXxtZm6xKHnmyyhCAOr/vCG3puXGzgYzMhijYA463FASencPU1l6+gAhKmWuPMEMmXWRmLwdXwvfFvGV15EAuUyHvq0HUudDMYoND6UjGNsoOC3CcQoW3HFh5mLHEYPETB6VxM5uuyjFUmeN6VkarX0C";
        User user = Graph.getUser(token);
        return user.displayName;
    }

    @GetMapping("/getToken")
    public String getToken() {
        return getUserAccessToken(scopes);
    }

    public String getUserAccessToken(String[] scopes) {
        Set<String> scopeSet = Sets.newHashSet(scopes);

        ExecutorService pool = Executors.newFixedThreadPool(1);

        PublicClientApplication app;
        try {
            // Build the MSAL application object with
            // app ID and authority
            app = PublicClientApplication.builder(appId)
                    .authority(AUTHORITY)
                    .executorService(pool)
                    .build();
        } catch (MalformedURLException e) {
            return null;
        }

        Consumer<DeviceCode> deviceCodeConsumer = (DeviceCode deviceCode) -> {
            // Print the login information to the console
            System.out.println(deviceCode.message());
        };

        // Request a token, passing the requested permission scopes
//        IAuthenticationResult result = app.acquireToken(
//                UserNamePasswordParameters.builder(scopeSet, userName, password.toCharArray()).build()
//        ).exceptionally(ex -> {
//            System.out.println("Unable to authenticate - " + ex.getMessage());
//            return null;
//        }).join();
        IAuthenticationResult result = app.acquireToken(
                DeviceCodeFlowParameters
                        .builder(scopeSet, deviceCodeConsumer)
                        .build()
        ).exceptionally(ex -> {
            System.out.println("Unable to authenticate - " + ex.getMessage());
            return null;
        }).join();

        pool.shutdown();

        if (result != null) {
            return result.accessToken();
        }

        return null;
    }
}
