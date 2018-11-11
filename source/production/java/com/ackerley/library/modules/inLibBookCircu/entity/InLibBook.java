package com.ackerley.library.modules.inLibBookCircu.entity;

import com.ackerley.library.common.entity.BaseEntity;
import com.ackerley.library.modules.sys.entity.Bookshelf;

/**
 * 在库书本
 */
public class InLibBook extends BaseEntity{
    private Biblio biblio;
    private Bookshelf bookshelf;

    private String state;
    public static final String ILBS_IN_STK = "in_lib_book_state_in_stock";  //在库在架
    public static final String ILBS_SHELVING = "in_lib_book_state_shelving";//在库待上架
    public static final String ILBS_OUT = "in_lib_book_state_lent_out";     //借出
    public static final String ILBS_LOST = "in_lib_book_state_lost";        //遗失

    private String barCode;         //条形码(采用ISBN13+3位序号(一目总不会超过1000本吧) 共16位，表意一下)

    public String getBarCode() {
        return barCode;
    }
    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }
    public Biblio getBiblio() {
        return biblio;
    }
    public void setBiblio(Biblio biblio) {
        this.biblio = biblio;
    }
    public Bookshelf getBookshelf() {
        return bookshelf;
    }
    public void setBookshelf(Bookshelf bookshelf) {
        this.bookshelf = bookshelf;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
}
