package com.ackerley.library.modules.inLibBookCircu.entity;

import com.ackerley.library.common.entity.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class BorrowReturnRecord extends BaseEntity {
    private String bookID;                      //书本
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date borrowTime;                    //借出时间
    private String libCrdID;                    //(借书人)借书证
    private String lendingTransactorID;         //借书经办人
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date returnTime;                    //归还时间
    private String returningTransactorID;       //还书经办人
    private boolean isRenewed;                          //是否已办理续借
    private boolean isOverdue;                          //是否已超期 ▲目前未用上  todo （学了定时计划任务后，用之来完成赋值，stage同时变化）
    private String stage;                       //借还事务的 阶段
    public static final String BRRS_OUT_NOT_OVERDUE = "lent_out_and_not_overdue";                           //书本外借，未超期
    public static final String BRRS_BACK_NOT_OVERDUE = "returned_back_and_not_overdue";                     //书本归还，未超期
    public static final String BRRS_OVERDUE_NOT_BACK = "overdue_and_not_returned_back";     //▲目前未用上    //书本外借，已超期
    public static final String BRRS_OVERDUE_BACK_FINE_UNPAID = "overdue_and_returned_back_and_fine_unpaid"; //书本归还，超期，罚金未缴
    public static final String BRRS_OVERDUE_BACK_FINE_PAID = "overdue_and_returned_back_and_fine_paid";     //书本归还，超期，罚金已缴
    //若图书遗失，遗失登记时不改动此stage，还可依照stage找到遗失责任人lib card...

    private String remarks;

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public Date getBorrowTime() {
        return borrowTime;
    }

    public void setBorrowTime(Date borrowTime) {
        this.borrowTime = borrowTime;
    }

    public String getLibCrdID() {
        return libCrdID;
    }

    public void setLibCrdID(String libCrdID) {
        this.libCrdID = libCrdID;
    }

    public String getLendingTransactorID() {
        return lendingTransactorID;
    }

    public void setLendingTransactorID(String lendingTransactorID) {
        this.lendingTransactorID = lendingTransactorID;
    }

    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

    public String getReturningTransactorID() {
        return returningTransactorID;
    }

    public void setReturningTransactorID(String returningTransactorID) {
        this.returningTransactorID = returningTransactorID;
    }

    public boolean getIsRenewed() {
        return isRenewed;
    }

    public void setRenewed(boolean renewed) {
        isRenewed = renewed;
    }

    public boolean getIsOverdue() {
        return isOverdue;
    }

    public void setOverdue(boolean overdue) {
        isOverdue = overdue;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    @Override
    public String getRemarks() {
        return remarks;
    }

    @Override
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
