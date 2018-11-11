package com.ackerley.library.modules.priorBookCircu.entity;

import com.ackerley.library.common.entity.BaseEntity;
import com.ackerley.library.common.entity.FieldRangeFilter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/*
* 采购单，内含 采购单items list...
*/
public class PBCPurOrder extends BaseEntity {
    private String name;                //采购单名
    private String creatorID;           //采购单创建者

    private Timestamp creationTime;        //采购单创建时间
    private FieldRangeFilter<Timestamp> creationTimeRangeFilter;  //creationTime的筛选区间

    private String inboundReviewerID;   //入库验收者

    private Timestamp inboundTime;      //入库验时间
    private FieldRangeFilter<Timestamp> inboundTimeRangeFilter;  //inboundTime的筛选区间

    private String orderState;          //采购单状态，"acquiring"(新建后，采购中)、"inLib"(已验收入库)，只有两个，就不写static final的constants了...mapper file中需注意统一

    private List<PBCPurOrderItem> list = new ArrayList<>(); //采购单items

    public Timestamp getInboundTime() {
        return inboundTime;
    }
    public void setInboundTime(Timestamp inboundTime) {
        this.inboundTime = inboundTime;
    }
    public FieldRangeFilter<Timestamp> getInboundTimeRangeFilter() {
        return inboundTimeRangeFilter;
    }
    public void setInboundTimeRangeFilter(FieldRangeFilter<Timestamp> inboundTimeRangeFilter) {
        this.inboundTimeRangeFilter = inboundTimeRangeFilter;
    }
    public FieldRangeFilter<Timestamp> getCreationTimeRangeFilter() {
        return creationTimeRangeFilter;
    }
    public void setCreationTimeRangeFilter(FieldRangeFilter<Timestamp> creationTimeRangeFilter) {
        this.creationTimeRangeFilter = creationTimeRangeFilter;
    }
    public String getCreatorID() {
        return creatorID;
    }
    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }
    public Timestamp getCreationTime() {
        return creationTime;
    }
    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }
    public String getInboundReviewerID() {
        return inboundReviewerID;
    }
    public void setInboundReviewerID(String inboundReviewerID) {
        this.inboundReviewerID = inboundReviewerID;
    }
    public String getOrderState() {
        return orderState;
    }
    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<PBCPurOrderItem> getList() {
        return list;
    }
    public void setList(List<PBCPurOrderItem> list) {
        this.list = list;
    }
}
