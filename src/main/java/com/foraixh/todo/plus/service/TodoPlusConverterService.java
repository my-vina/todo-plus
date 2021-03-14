package com.foraixh.todo.plus.service;

import com.foraixh.todo.plus.pojo.TodoPlusTask;
import com.foraixh.todo.plus.pojo.TodoPlusTaskList;
import com.google.gson.JsonObject;
import com.microsoft.graph.models.extensions.TodoTask;
import com.microsoft.graph.models.extensions.TodoTaskList;

/**
 * @author myvina@qq.com
 * @date 2021/3/14  13:53
 * @usage TodoPlus的task和taskList的转换服务
 */

public interface TodoPlusConverterService {
    /**
     * 将microsoft todoTask 转换为todoPlusTask
     * @param userName userName
     * @param todoTask microsoft的todoTask
     * @param taskListId microsoft的todoTaskListId
     * @return todoPlusTask
     */
    TodoPlusTask convertTodoToTodoPlusTask(String userName, TodoTask todoTask, String taskListId);

    /**
     * 将microsoft todoTaskList 转换为todoPlusTaskList
     * @param userName userName
     * @param todoTaskList microsoft的todoTaskList
     * @return todoPlusTaskList
     */
    TodoPlusTaskList convertTodoToTodoPlusTaskList(String userName, TodoTaskList todoTaskList);
}
