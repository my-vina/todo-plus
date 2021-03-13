package com.foraixh.todo.plus.pojo;

/**
 * @author myvina
 * @date 2021/03/12 09:37
 * @usage
 */
@SuppressWarnings("unused")
public class TodoTaskList {
    private String id;

    private String odataEtag;

    private String displayName;

    private boolean isOwner;

    private boolean isShared;

    private String wellknownListName;

    public TodoTaskList() {
    }

    public String getOdataEtag() {
        return odataEtag;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean getIsOwner() {
        return isOwner;
    }

    public boolean getIsShared() {
        return isShared;
    }

    public String getWellknownListName() {
        return wellknownListName;
    }

    public String getId() {
        return id;
    }

    public void setOdataEtag(String odataEtag) {
        this.odataEtag = odataEtag;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setIsOwner(boolean isOwner) {
        this.isOwner = isOwner;
    }

    public void setIsShared(boolean isShared) {
        this.isShared = isShared;
    }

    public void setWellknownListName(String wellknownListName) {
        this.wellknownListName = wellknownListName;
    }

    public void setId(String id) {
        this.id = id;
    }
}
