package com.foraixh.todo.plus.controller;

import com.foraixh.todo.plus.response.GlobalResponse;
import com.foraixh.todo.plus.service.TodoListService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author myvina
 * @date 2021/03/12 11:11
 * @usage
 */

@RestController
@RequestMapping("/todo")
public class TodoTaskController {
    private static final Logger log = getLogger(TodoTaskController.class);

    private final TodoListService todoListService;

    public TodoTaskController(TodoListService todoListService) {
        this.todoListService = todoListService;
    }

    @PostMapping("/task/{todoListId}")
    public GlobalResponse<List<JsonObject>> todoTaskList(String userName, @PathVariable String todoListId) {
        return GlobalResponse.success(todoListService.myTodoTask(userName, todoListId));
    }

    @PostMapping("/taskList")
    public GlobalResponse<List<JsonObject>> todoList(String userName) {
        return GlobalResponse.success(todoListService.myTodoList(userName));
    }
}
