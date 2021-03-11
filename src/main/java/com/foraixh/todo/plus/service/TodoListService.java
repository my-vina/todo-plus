package com.foraixh.todo.plus.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.aad.msal4j.IAuthenticationResult;

/**
 * @author myvina
 * @date 2021/03/10 10:52
 * @usage todolist服务类
 */
public interface TodoListService {
    /**
     * 我的todo任务列表
     * @param token token
     * @return todo任务列表
     */
    JsonArray myTodoList(String token);

    /**
     * 我的特定todo任务列表里面的任务
     * @param token token
     * @param todoTaskListId todo任务列表id
     * @return todo任务列表里面的任务
     */
    JsonArray myTodoTask(String token, String todoTaskListId);
}
