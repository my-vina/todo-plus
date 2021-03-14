package com.foraixh.todo.plus.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author myvina@qq.com
 * @date 2021/3/14  13:38
 * @usage
 */

public class TodoPlusBaseBean implements Serializable {
    public String userName;

    /**
     * The Id.
     * Read-only.
     */
    @SerializedName(value = "id", alternate = {"Id"})
    @Expose
    public String todoPlusId;
}
