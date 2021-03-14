package com.foraixh.todo.plus.service.impl;

import com.foraixh.todo.plus.pojo.TodoPlusTask;
import com.foraixh.todo.plus.pojo.TodoPlusTaskList;
import com.foraixh.todo.plus.service.TodoPlusConverterService;
import com.microsoft.graph.models.extensions.TodoTask;
import com.microsoft.graph.models.extensions.TodoTaskList;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.SimpleTimeZone;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author myvina@qq.com
 * @date 2021/3/14  13:56
 * @usage
 */

@Service
public class TodoPlusConverterServiceImpl implements TodoPlusConverterService {
    private static final Logger log = getLogger(TodoPlusConverterServiceImpl.class);

    @Override
    public TodoPlusTask convertTodoToTodoPlusTask(String userName, TodoTask todoTask, String taskListId) {
        TodoPlusTask todoPlusTask = TodoPlusTask.TodoPlusTaskBuilder.builder()
                .taskListId(taskListId)
                .content(todoTask.body.content)
                .contentType(todoTask.body.contentType)
                .bodyLastModifiedDateTime(1L)
                .completedDateTime(1L)
                .createdDateTime(1L)
                .dueDateTime(1L)
                .importance(todoTask.importance)
                .isReminderOn(todoTask.isReminderOn)
                .lastModifiedDateTime(1L)
                .reminderDateTime(1L)
                .status(todoTask.status)
                .title(todoTask.title)
                .userName(userName)
                .todoPlusId(todoTask.id)
                .build();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

            if (Objects.nonNull(todoTask.bodyLastModifiedDateTime)) {
                todoPlusTask.bodyLastModifiedDateTime = todoTask.bodyLastModifiedDateTime.getTimeInMillis();
            }
            if (Objects.nonNull(todoTask.completedDateTime)) {
                dateFormat.setTimeZone(SimpleTimeZone.getTimeZone(todoTask.completedDateTime.timeZone));
                todoPlusTask.lastModifiedDateTime = dateFormat.parse(todoTask.completedDateTime.dateTime).getTime();
            }
            if (Objects.nonNull(todoTask.createdDateTime)) {
                todoPlusTask.createdDateTime = todoTask.createdDateTime.getTimeInMillis();
            }
            if (Objects.nonNull(todoTask.dueDateTime)) {
                dateFormat.setTimeZone(SimpleTimeZone.getTimeZone(todoTask.dueDateTime.timeZone));
                todoPlusTask.dueDateTime = dateFormat.parse(todoTask.dueDateTime.dateTime).getTime();
            }
            if (Objects.nonNull(todoTask.lastModifiedDateTime)) {
                todoPlusTask.lastModifiedDateTime = todoTask.lastModifiedDateTime.getTimeInMillis();
            }
            if (Objects.nonNull(todoTask.reminderDateTime)) {
                dateFormat.setTimeZone(SimpleTimeZone.getTimeZone(todoTask.reminderDateTime.timeZone));
                todoPlusTask.reminderDateTime = dateFormat.parse(todoTask.reminderDateTime.dateTime).getTime();
            }
        } catch (ParseException e) {
            log.error("日期格式转换错误", e);
        }

        return todoPlusTask;
    }

    @Override
    public TodoPlusTaskList convertTodoToTodoPlusTaskList(String userName, TodoTaskList todoTaskList) {
        return TodoPlusTaskList.TodoPlusTaskListBuilder.builder()
                .displayName(todoTaskList.displayName)
                .isOwner(todoTaskList.isOwner)
                .isShared(todoTaskList.isShared)
                .wellknownListName(todoTaskList.wellknownListName)
                .userName(userName)
                .todoPlusId(todoTaskList.id)
                .build();
    }
}
