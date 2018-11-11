package com.ackerley.library.modules.inLibBookCircu.entity;

import java.util.ArrayList;
import java.util.List;

/*
* 用于实现批量待上架图书项的 页面投放 与 入参绑定 的辅助类，内置list(所谓批量)，list item为 pair构造：一本待上架图书 搭配 一个对应的上架结果信息...
*
*/
public class BulkBookShelvingAid {
    private List<BookShelvingResultPair> list = new ArrayList<>();

    public BulkBookShelvingAid() {}

    public BulkBookShelvingAid(List<InLibBook> bookList) {
        for (InLibBook book : bookList) {
            list.add(new BookShelvingResultPair(book));
        }
    }

    public List<BookShelvingResultPair> getList() {
        return list;
    }
    public void setList(List<BookShelvingResultPair> list) {
        this.list = list;
    }
}
