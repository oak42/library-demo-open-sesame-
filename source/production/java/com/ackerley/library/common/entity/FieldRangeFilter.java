package com.ackerley.library.common.entity;

import java.util.Map;

public class FieldRangeFilter<T> {
    private T from;
    private T to;

//    FieldRangeFilter(){}

    FieldRangeFilter(T from, T to){
        this.from = from;
        this.to = to;
    }

    public T getFrom() {
        return from;
    }
    public void setFrom(T from) {
        this.from = from;
    }

    public T getTo() {
        return to;
    }
    public void setTo(T to) {
        this.to = to;
    }
}
