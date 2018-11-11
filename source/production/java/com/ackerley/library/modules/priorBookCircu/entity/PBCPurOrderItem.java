package com.ackerley.library.modules.priorBookCircu.entity;

import com.ackerley.library.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonProperty;

/*
* 采购单明细项
*/
public class PBCPurOrderItem extends BaseEntity {
    private String purOrderID;      //所属采购单ID
    @JsonProperty(value="ISBN13")
    private String ISBN13;          //
    private String title;           //其实仅需ISBN13即可identify，添个title便于human readable...
    private int orderQty;           //计划采购量【关于range filter，是否所有的 线性量 都潜在地适合配一个range filter以便在SQL中使用 起止范围 来筛选？】
    private int inboundQty;         //实际采购量

    public PBCPurOrderItem(){}
    public PBCPurOrderItem(PBCProcInstc procInstc) {
        this.ISBN13 = procInstc.getISBN13();
        this.title = procInstc.getTitle();
    }

    public String getPurOrderID() {
        return purOrderID;
    }
    public void setPurOrderID(String purOrderID) {
        this.purOrderID = purOrderID;
    }
    public String getISBN13() {
        return ISBN13;
    }
    public void setISBN13(String ISBN13) {
        this.ISBN13 = ISBN13;
    }
    public int getOrderQty() {
        return orderQty;
    }
    public void setOrderQty(int orderQty) {
        this.orderQty = orderQty;
    }
    public int getInboundQty() {
        return inboundQty;
    }
    public void setInboundQty(int inboundQty) {
        this.inboundQty = inboundQty;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}