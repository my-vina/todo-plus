package com.foraixh.todo.plus.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.microsoft.graph.models.generated.WellknownListName;

/**
 * @author myvina@qq.com
 * @date 2021/3/14  13:39
 * @usage
 */

public class TodoPlusTaskList extends TodoPlusBaseBean {
    /**
     * The Display Name.
     * The name of the task list.
     */
    @SerializedName(value = "displayName", alternate = {"DisplayName"})
    @Expose
    public String displayName;

    /**
     * The Is Owner.
     * True if the user is owner of the given task list.
     */
    @SerializedName(value = "isOwner", alternate = {"IsOwner"})
    @Expose
    public Boolean isOwner;

    /**
     * The Is Shared.
     * True if the task list is shared with other users
     */
    @SerializedName(value = "isShared", alternate = {"IsShared"})
    @Expose
    public Boolean isShared;

    /**
     * The Wellknown List Name.
     * Property indicating the list name if the given list is a well-known list. Possible values are: none, defaultList, flaggedEmails, unknownFutureValue.
     */
    @SerializedName(value = "wellknownListName", alternate = {"WellknownListName"})
    @Expose
    public WellknownListName wellknownListName;

    public static final class TodoPlusTaskListBuilder {
        public String displayName;
        public Boolean isOwner;
        public Boolean isShared;
        public WellknownListName wellknownListName;
        public String todoPlusId;
        public String userName;

        private TodoPlusTaskListBuilder() {
        }

        public static TodoPlusTaskListBuilder builder() {
            return new TodoPlusTaskListBuilder();
        }

        public TodoPlusTaskListBuilder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public TodoPlusTaskListBuilder isOwner(Boolean isOwner) {
            this.isOwner = isOwner;
            return this;
        }

        public TodoPlusTaskListBuilder isShared(Boolean isShared) {
            this.isShared = isShared;
            return this;
        }

        public TodoPlusTaskListBuilder wellknownListName(WellknownListName wellknownListName) {
            this.wellknownListName = wellknownListName;
            return this;
        }

        public TodoPlusTaskListBuilder todoPlusId(String todoPlusId) {
            this.todoPlusId = todoPlusId;
            return this;
        }

        public TodoPlusTaskListBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public TodoPlusTaskList build() {
            TodoPlusTaskList todoPlusTaskList = new TodoPlusTaskList();
            todoPlusTaskList.userName = this.userName;
            todoPlusTaskList.wellknownListName = this.wellknownListName;
            todoPlusTaskList.isShared = this.isShared;
            todoPlusTaskList.todoPlusId = this.todoPlusId;
            todoPlusTaskList.isOwner = this.isOwner;
            todoPlusTaskList.displayName = this.displayName;
            return todoPlusTaskList;
        }
    }
}
