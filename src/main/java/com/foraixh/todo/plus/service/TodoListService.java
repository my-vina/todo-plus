package com.foraixh.todo.plus.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.aad.msal4j.IAuthenticationResult;

import java.util.List;

/**
 * @author myvina
 * @date 2021/03/10 10:52
 * @usage todolist服务类
 */
public interface TodoListService {
    /**
     * 我的todo任务列表
     * @param userName userName
     * @return todo任务列表
     */
    List<JsonObject> myTodoList(String userName);

    /**
     * 我的特定todo任务列表里面的任务
     * @param userName userName
     * @param todoTaskListId todo任务列表id
     * @return todo任务列表里面的任务
     */
    List<JsonObject> myTodoTask(String userName, String todoTaskListId);

    /**
     * 简单地同步用户下的todo任务列表和任务
     * @param userName userName
     */
    void simpleSyncTodo(String userName);
}
