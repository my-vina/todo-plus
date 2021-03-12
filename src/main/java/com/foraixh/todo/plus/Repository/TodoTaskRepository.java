package com.foraixh.todo.plus.Repository;

import com.foraixh.todo.plus.constant.TodoTaskTableConstants;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author myvina
 * @date 2021/03/12 09:37
 * @usage
 */

@Repository
public class TodoTaskRepository {
    private static final Logger log = LoggerFactory.getLogger(TodoTaskRepository.class);

    private final MongoTemplate mongoTemplate;

    public TodoTaskRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public int insert(JsonArray jsonArray, String collectionName) {
        List<JsonElement> list = new LinkedList<>();
        jsonArray.forEach(list::add);
        return mongoTemplate.insert(list, collectionName).size();
    }

    public List<JsonObject> selectAllTodoTaskList(String userName) {
        return mongoTemplate.find(Query.query(Criteria.where("userName").is(userName)), JsonObject.class,
                TodoTaskTableConstants.TODO_TASK_LIST_TABLE);
    }
}
