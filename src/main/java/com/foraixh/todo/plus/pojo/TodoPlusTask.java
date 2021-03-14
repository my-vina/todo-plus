package com.foraixh.todo.plus.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.microsoft.graph.models.generated.BodyType;
import com.microsoft.graph.models.generated.Importance;
import com.microsoft.graph.models.generated.TaskStatus;

/**
 * @author myvina@qq.com
 * @date 2021/3/14  13:43
 * @usage
 */

public class TodoPlusTask extends TodoPlusBaseBean {
    /**
     * taskListId
     */
    public String taskListId;

    /**
     * The Content.
     * The content of the item.
     */
    @SerializedName(value = "content", alternate = {"Content"})
    @Expose
    public String content;

    /**
     * The Content Type.
     * The type of the content. Possible values are text and html.
     */
    @SerializedName(value = "contentType", alternate = {"ContentType"})
    @Expose
    public BodyType contentType;

    /**
     * The Body Last Modified Date Time.
     * The date and time when the task was last modified. By default, it is in UTC. You can provide a custom time zone in the request header. The property value uses ISO 8601 format and is always in UTC time. For example, midnight UTC on Jan 1, 2020 would look like this: '2020-01-01T00:00:00Z'.
     */
    public Long bodyLastModifiedDateTime;

    /**
     * The Completed Date Time.
     * The date in the specified time zone that the task was finished.
     */
    public Long completedDateTime;

    /**
     * The Created Date Time.
     * The date and time when the task was created. By default, it is in UTC. You can provide a custom time zone in the request header. The property value uses ISO 8601 format. For example, midnight UTC on Jan 1, 2020 would look like this: '2020-01-01T00:00:00Z'.
     */
    public Long createdDateTime;

    /**
     * The Due Date Time.
     * The date in the specified time zone that the task is to be finished.
     */
    public Long dueDateTime;

    /**
     * The Importance.
     * The importance of the task. Possible values are: low, normal, high.
     */
    @SerializedName(value = "importance", alternate = {"Importance"})
    @Expose
    public Importance importance;

    /**
     * The Is Reminder On.
     * Set to true if an alert is set to remind the user of the task.
     */
    @SerializedName(value = "isReminderOn", alternate = {"IsReminderOn"})
    @Expose
    public Boolean isReminderOn;

    /**
     * The Last Modified Date Time.
     * The date and time when the task was last modified. By default, it is in UTC. You can provide a custom time zone in the request header. The property value uses ISO 8601 format and is always in UTC time. For example, midnight UTC on Jan 1, 2020 would look like this: '2020-01-01T00:00:00Z'.
     */
    public Long lastModifiedDateTime;

    // TODO 任务重复机制

    /**
     * The Reminder Date Time.
     * The date and time for a reminder alert of the task to occur.
     */
    public Long reminderDateTime;

    /**
     * The Status.
     * Indicates the state or progress of the task. Possible values are: notStarted, inProgress, completed, waitingOnOthers, deferred.
     */
    @SerializedName(value = "status", alternate = {"Status"})
    @Expose
    public TaskStatus status;

    /**
     * The Title.
     * A brief description of the task.
     */
    @SerializedName(value = "title", alternate = {"Title"})
    @Expose
    public String title;

    public static final class TodoPlusTaskBuilder {
        public String taskListId;
        public String content;
        public BodyType contentType;
        public Long bodyLastModifiedDateTime;
        public Long completedDateTime;
        public Long createdDateTime;
        public Long dueDateTime;
        public Importance importance;
        public Boolean isReminderOn;
        public Long lastModifiedDateTime;
        public Long reminderDateTime;
        public TaskStatus status;
        public String title;
        public String userName;
        public String todoPlusId;

        private TodoPlusTaskBuilder() {
        }

        public static TodoPlusTaskBuilder builder() {
            return new TodoPlusTaskBuilder();
        }

        public TodoPlusTaskBuilder taskListId(String taskListId) {
            this.taskListId = taskListId;
            return this;
        }

        public TodoPlusTaskBuilder content(String content) {
            this.content = content;
            return this;
        }

        public TodoPlusTaskBuilder contentType(BodyType contentType) {
            this.contentType = contentType;
            return this;
        }

        public TodoPlusTaskBuilder bodyLastModifiedDateTime(Long bodyLastModifiedDateTime) {
            this.bodyLastModifiedDateTime = bodyLastModifiedDateTime;
            return this;
        }

        public TodoPlusTaskBuilder completedDateTime(Long completedDateTime) {
            this.completedDateTime = completedDateTime;
            return this;
        }

        public TodoPlusTaskBuilder createdDateTime(Long createdDateTime) {
            this.createdDateTime = createdDateTime;
            return this;
        }

        public TodoPlusTaskBuilder dueDateTime(Long dueDateTime) {
            this.dueDateTime = dueDateTime;
            return this;
        }

        public TodoPlusTaskBuilder importance(Importance importance) {
            this.importance = importance;
            return this;
        }

        public TodoPlusTaskBuilder isReminderOn(Boolean isReminderOn) {
            this.isReminderOn = isReminderOn;
            return this;
        }

        public TodoPlusTaskBuilder lastModifiedDateTime(Long lastModifiedDateTime) {
            this.lastModifiedDateTime = lastModifiedDateTime;
            return this;
        }

        public TodoPlusTaskBuilder reminderDateTime(Long reminderDateTime) {
            this.reminderDateTime = reminderDateTime;
            return this;
        }

        public TodoPlusTaskBuilder status(TaskStatus status) {
            this.status = status;
            return this;
        }

        public TodoPlusTaskBuilder title(String title) {
            this.title = title;
            return this;
        }

        public TodoPlusTaskBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public TodoPlusTaskBuilder todoPlusId(String todoPlusId) {
            this.todoPlusId = todoPlusId;
            return this;
        }

        public TodoPlusTask build() {
            TodoPlusTask todoPlusTask = new TodoPlusTask();
            todoPlusTask.userName = this.userName;
            todoPlusTask.isReminderOn = this.isReminderOn;
            todoPlusTask.content = this.content;
            todoPlusTask.reminderDateTime = this.reminderDateTime;
            todoPlusTask.lastModifiedDateTime = this.lastModifiedDateTime;
            todoPlusTask.importance = this.importance;
            todoPlusTask.bodyLastModifiedDateTime = this.bodyLastModifiedDateTime;
            todoPlusTask.taskListId = this.taskListId;
            todoPlusTask.contentType = this.contentType;
            todoPlusTask.todoPlusId = this.todoPlusId;
            todoPlusTask.completedDateTime = this.completedDateTime;
            todoPlusTask.dueDateTime = this.dueDateTime;
            todoPlusTask.title = this.title;
            todoPlusTask.createdDateTime = this.createdDateTime;
            todoPlusTask.status = this.status;
            return todoPlusTask;
        }
    }
}
