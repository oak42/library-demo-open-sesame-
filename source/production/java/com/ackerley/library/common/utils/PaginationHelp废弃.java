//废弃...
// 分页helper class，存储 分页信息...这种占内存的分页不被大家采用的吧？一般都是用DB限制返回的记录的吧...

package com.ackerley.library.common.utils;

import java.util.ArrayList;
import java.util.List;

public class PaginationHelp废弃 {
    private long total;             //总item数...
    private int perPage;            //每页item数...

    private int currentPageNum;    //当前页，始于1...

    private int totalPages;        //

    private List<String> pageList;  //页码表...

    private int blankTail;          //

    public PaginationHelp废弃(){}       //给data binding用的？...

    public PaginationHelp废弃(long total, int perPage){
        this.total = total;
        this.perPage = perPage;
        this.totalPages = (int)(Math.floorMod(total, perPage) == 0 ? total/perPage : total/perPage + 1);    //若total为0，totalPages也为0；另：cast是一个缺陷？...
        this.currentPageNum = totalPages == 0 ? 0 : 1;                                                  //
        this.blankTail = (int)(this.totalPages * perPage - total);

        this.pageList = new ArrayList<>();
        for (int i = 0; i < totalPages; i++) {
            pageList.add(String.valueOf(i + 1));
        }
    }

    public long getTotal() {
        return total;
    }

    public int getPerPage() {
        return perPage;
    }

    public int getCurrentPageNum() {
        return currentPageNum;
    }

    public void setCurrentPageNum(int currentPageNum) {
        this.currentPageNum = currentPageNum;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public int getBlankTail() {
        return blankTail;
    }

    public List<String> getPageList() {
        return pageList;
    }

}
