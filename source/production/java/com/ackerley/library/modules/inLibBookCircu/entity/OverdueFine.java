package com.ackerley.library.modules.inLibBookCircu.entity;

import com.ackerley.library.common.entity.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/*
* 单本书的罚金信息
*/

public class OverdueFine extends BaseEntity{
    private String borrowAndReturnRecordID;         //相关借还记录ID
    private float amount;                           //罚金金额
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date formationTime;                     //罚金形成时间(逾期图书还书日)
    private String libCrdID;                        //借书证ID，其实有些冗余了(为了方便些吧)，borrowAndReturnRecordID可以找到借还记录，借还记录有借书证ID
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date receivingTime;                     //罚金收取日期
    private String receivingAgentID;                //罚金收取人ID
    private String state;                           //"unpaid"、"paid"...


    public String getBorrowAndReturnRecordID() {
        return borrowAndReturnRecordID;
    }

    public void setBorrowAndReturnRecordID(String borrowAndReturnRecordID) {
        this.borrowAndReturnRecordID = borrowAndReturnRecordID;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Date getFormationTime() {
        return formationTime;
    }

    public void setFormationTime(Date formationTime) {
        this.formationTime = formationTime;
    }

    public String getLibCrdID() {
        return libCrdID;
    }

    public void setLibCrdID(String libCrdID) {
        this.libCrdID = libCrdID;
    }

    public Date getReceivingTime() {
        return receivingTime;
    }

    public void setReceivingTime(Date receivingTime) {
        this.receivingTime = receivingTime;
    }

    public String getReceivingAgentID() {
        return receivingAgentID;
    }

    public void setReceivingAgentID(String receivingAgentID) {
        this.receivingAgentID = receivingAgentID;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
