package com.ackerley.library.common.entity;

import java.util.ArrayList;
import java.util.List;

public class SimpleList<T> {
    private List<T> list;   //【spring MVC不支持？不会data bind了？不会instantiate泛型对象？但是有擦除的啊？！】Invalid property 'list[0]' of bean class [com.ackerley.library.common.entity.SimpleList]: Index of out of bounds in property path 'list[0]'; nested exception is java.lang.IndexOutOfBoundsException: Index: 0, Size: 0

    public SimpleList() { list = new ArrayList<>(); }
    public SimpleList(List<T> list) {
        this();
        this.list.addAll(list);
    }

    public List<T> getList() {
        return list;
    }
    public void setList(List<T> list) {
        this.list = list;
    }
}
