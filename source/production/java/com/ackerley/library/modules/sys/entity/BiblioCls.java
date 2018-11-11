package com.ackerley.library.modules.sys.entity;

import com.ackerley.library.common.entity.BaseEntity;

public class BiblioCls extends BaseEntity{  //TODO【扣】缩水了，其实应当继承TreeEntity(TreeEntity继承自BaseEntity)，TreeEntity中统一封装tree型对象的CRUD逻辑，具体参见jeesite...keyword("extends TreeEntity")可查到相关重构痕迹...
    private String notation;
    private String name;
    private String parentID;
    private String ancestorIDs;



    public String getNotation() {
        return notation;
    }
    public void setNotation(String notation) {
        this.notation = notation;
    }
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
    public String getAncestorIDs() {
        return ancestorIDs;
    }
    public void setAncestorIDs(String ancestorIDs) {
        this.ancestorIDs = ancestorIDs;
    }
}
