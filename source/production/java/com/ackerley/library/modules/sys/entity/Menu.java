package com.ackerley.library.modules.sys.entity;

import com.ackerley.library.common.entity.BaseEntity;

/**
 * Created by ackerley on 2018/5/9.
 */
public class Menu extends BaseEntity{
    private String name;
    private String parentID;   //immediate parent menu item id...
    private String href;
    private String requiredPermissions;
//    private String icon;
//    private String target;
    private boolean presenceFlag;//【扣】varchar(1) → boolean 可以吗，目前未暴露?...纯粹放权限的menu record是不显示的，presence_flag false...

    private String localOrdering;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getRequiredPermissions() {
        return requiredPermissions;
    }

    public void setRequiredPermissions(String requiredPermissions) {
        this.requiredPermissions = requiredPermissions;
    }

    public boolean isPresenceFlag() {
        return presenceFlag;
    }

    public void setPresenceFlag(boolean presenceFlag) {
        this.presenceFlag = presenceFlag;
    }

    public String getLocalOrdering() {
        return localOrdering;
    }

    public void setLocalOrdering(String localOrdering) {
        this.localOrdering = localOrdering;
    }
}
