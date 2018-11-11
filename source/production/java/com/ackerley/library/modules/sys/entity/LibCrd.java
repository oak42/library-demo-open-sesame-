package com.ackerley.library.modules.sys.entity;

import com.ackerley.library.common.entity.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/*
* 图书借阅证
*/
public class LibCrd extends BaseEntity{
    private String ownerID;              //持有人ID
    private String hrefOwnerPhoto;       //持有人证件照
    private String barCode;             //借书证条形码
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date issueDate;             //发证日期
    private String issuingTransactorID; //发证经办人

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getHrefOwnerPhoto() {
        return hrefOwnerPhoto;
    }

    public void setHrefOwnerPhoto(String hrefOwnerPhoto) {
        this.hrefOwnerPhoto = hrefOwnerPhoto;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getIssuingTransactorID() {
        return issuingTransactorID;
    }

    public void setIssuingTransactorID(String issuingTransactorID) {
        this.issuingTransactorID = issuingTransactorID;
    }

}
