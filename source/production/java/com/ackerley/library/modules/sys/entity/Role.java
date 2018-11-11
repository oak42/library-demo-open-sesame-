package com.ackerley.library.modules.sys.entity;

import com.ackerley.library.common.entity.BaseEntity;

/**
 * 乞丐版role
 */
public class Role extends BaseEntity {
    private String name;    //角色名

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
