package com.foraixh.todo.plus.Repository;

import com.foraixh.todo.plus.constant.TodoTaskTableConstants;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.BulkOperations;
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

    public int insert(List<JsonObject> list, String collectionName) {
        return mongoTemplate.insert(list, collectionName).size();
    }

    public long deleteByUserName(String userName, String collectionName) {
        return mongoTemplate.remove(Query.query(Criteria.where("userName").in(userName)), collectionName).getDeletedCount();
    }

    public long deleteById(List<String> idList, String collectionName) {
        return mongoTemplate.remove(Query.query(Criteria.where("id").in(idList)), collectionName).getDeletedCount();
    }

    public long deleteByTodoTaskListId(List<String> idList, String collectionName) {
        return mongoTemplate.remove(Query.query(Criteria.where("todoTaskListId").in(idList)), collectionName).getDeletedCount();
    }

    public List<JsonObject> selectAllTodoTaskListByUserName(String userName) {
        return mongoTemplate.find(Query.query(Criteria.where("userName").is(userName)), JsonObject.class,
                TodoTaskTableConstants.TODO_TASK_LIST_TABLE);
    }

    public List<JsonObject> selectAllTodoTaskByUserNameAndTodoTaskListId(String userName, String todoTaskListId) {
        return mongoTemplate.find(Query.query(Criteria.where("userName").is(userName).and("todoTaskListId").is(todoTaskListId)),
                JsonObject.class, TodoTaskTableConstants.TODO_TASK_TABLE);
    }

    public void syncTodoTaskList(String userName, List<JsonObject> latestList) {
        long size;

        // 删除数据库中todoTaskList数据
        size = deleteByUserName(userName, TodoTaskTableConstants.TODO_TASK_LIST_TABLE);
        log.info("删除{}条todoTaskList记录", size);

        // 插入microsoft最新数据
        if ((size = insert(latestList, TodoTaskTableConstants.TODO_TASK_LIST_TABLE)) != latestList.size()) {
            throw new RuntimeException("插入的todoTaskList数量不一致；插入数量为: " + size);
        }
        log.info("插入{}条todoTaskList记录", size);
    }

    public void syncTodoTask(String userName, List<JsonObject> latestList) {
        long size;

        // 删除数据库中todoTaskList数据
        size = deleteByUserName(userName, TodoTaskTableConstants.TODO_TASK_TABLE);
        log.info("删除{}条todoTask记录", size);

        // 插入microsoft最新数据
        if ((size = insert(latestList, TodoTaskTableConstants.TODO_TASK_TABLE)) != latestList.size()) {
            throw new RuntimeException("插入的todoTask数量不一致；插入数量为: " + size);
        }
        log.info("插入{}条todoTask记录", size);
    }
}
