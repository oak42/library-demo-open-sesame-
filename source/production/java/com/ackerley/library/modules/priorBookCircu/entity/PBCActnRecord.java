package com.ackerley.library.modules.priorBookCircu.entity;

import com.ackerley.library.common.entity.BaseEntity;
import com.ackerley.library.common.entity.FieldRangeFilter;

import java.sql.Timestamp;

/*
* 入库前流转 流程实例 下的 事务行为记录
* action = 前流程stage + 具体action (以':'分隔)
*/

public class PBCActnRecord extends BaseEntity{
    private String procID;          //所属流程实例ID

    private String action;          //具体行为
    public static final String PCA_INI = "prior_circu_actn_initiate";
    public static final String PCA_APRV = "prior_circu_actn_approve";   //审核员行为
    public static final String PCA_RJCT = "prior_circu_actn_reject";    //审核员行为
    public static final String PCA_INBOUND = "prior_circu_actn_inbound_confirm";//采购入库的验收确认
    public static final String PCA_DONE = "prior_circu_actn_done";      //任务类型的行为的完成

    private Timestamp actionTime;
    private FieldRangeFilter<Timestamp> actionTimeRangeFilter;  //actionTime的筛选区间 (根据PBC流程action是否所处指定时间段 查询action时 使用)

    private String doerID;          //行为人

    public PBCActnRecord(){}
    public PBCActnRecord(String procID, String action, Timestamp actionTime, String doerID) {
        this.procID = procID;
        this.action = action;
        this.actionTime = actionTime;
        this.doerID = doerID;
    }

    public PBCActnRecord(String ID, String procID, String action, Timestamp actionTime, String doerID, String remarks) {
        super.ID = ID;
        super.remarks = remarks;

        this.procID = procID;
        this.action = action;
        this.actionTime = actionTime;
        this.doerID = doerID;
    }

    public FieldRangeFilter<Timestamp> getActionTimeRangeFilter() {
        return actionTimeRangeFilter;
    }
    public void setActionTimeRangeFilter(FieldRangeFilter<Timestamp> actionTimeRangeFilter) {
        this.actionTimeRangeFilter = actionTimeRangeFilter;
    }

    public Timestamp getActionTime() {
        return actionTime;
    }
    public void setActionTime(Timestamp actionTime) {
        this.actionTime = actionTime;
    }

    public String getProcID() {
        return procID;
    }
    public void setProcID(String procID) {
        this.procID = procID;
    }

    public String getDoerID() {
        return doerID;
    }
    public void setDoerID(String doerID) {
        this.doerID = doerID;
    }

    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
}
