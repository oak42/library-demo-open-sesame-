package com.ackerley.library.modules.sys.entity;

import com.ackerley.library.common.entity.BaseEntity;

/**
 * Created by ackerley on 2018/5/14.
 */
public class Bookshelf extends BaseEntity {
    private String label;    //书架标签
    private String location; //书架位置

    public Bookshelf(){}//【扣】若无无参构造器，mybatis、spring这类框架在创建对象时会报错？！

    public Bookshelf(String label, String location){
        this.label = label;
        this.location = location;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
