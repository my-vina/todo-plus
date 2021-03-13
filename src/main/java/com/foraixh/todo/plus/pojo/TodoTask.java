package com.foraixh.todo.plus.pojo;

import com.google.common.base.Objects;

import java.util.Date;

/**
 * @author myvina
 * @date 2021/03/12 09:37
 * @usage
 */
@SuppressWarnings("unused")
public class TodoTask {
    private String importance;

    private boolean isReminderOn;

    private String status;

    private String title;

    private Date createdDateTime;

    private Date lastModifiedDateTime;

    private String id;

    private Body body;

    private Date dueDateTime;

    private Date reminderDateTime;

    private String userName;

    private String todoTaskListId;

    public TodoTask() {
    }

    public String getImportance() {
        return importance;
    }

    public boolean getIsReminderOn() {
        return isReminderOn;
    }

    public String getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public Date getLastModifiedDateTime() {
        return lastModifiedDateTime;
    }

    public String getId() {
        return id;
    }

    public Body getBody() {
        return body;
    }

    public Date getDueDateTime() {
        return dueDateTime;
    }

    public Date getReminderDateTime() {
        return reminderDateTime;
    }

    public String getUserName() {
        return userName;
    }

    public String getTodoTaskListId() {
        return todoTaskListId;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }

    public void setIsReminderOn(boolean isReminderOn) {
        this.isReminderOn = isReminderOn;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public void setLastModifiedDateTime(Date lastModifiedDateTime) {
        this.lastModifiedDateTime = lastModifiedDateTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public void setDueDateTime(Date dueDateTime) {
        this.dueDateTime = dueDateTime;
    }

    public void setReminderDateTime(Date reminderDateTime) {
        this.reminderDateTime = reminderDateTime;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setTodoTaskListId(String todoTaskListId) {
        this.todoTaskListId = todoTaskListId;
    }



    @SuppressWarnings("unused")
    public static class Body {
        private String content;

        private String contentType;

        public Body() {
        }

        public String getContent() {
            return content;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        @Override
        public String toString() {
            return "Body{" +
                    "content='" + content + '\'' +
                    ", contentType='" + contentType + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "TodoTask{" +
                "importance='" + importance + '\'' +
                ", isReminderOn=" + isReminderOn +
                ", status='" + status + '\'' +
                ", title='" + title + '\'' +
                ", createdDateTime=" + createdDateTime +
                ", lastModifiedDateTime=" + lastModifiedDateTime +
                ", id='" + id + '\'' +
                ", body=" + body +
                ", dueDateTime=" + dueDateTime +
                ", reminderDateTime=" + reminderDateTime +
                ", userName='" + userName + '\'' +
                ", todoTaskListId='" + todoTaskListId + '\'' +
                '}';
    }
}
