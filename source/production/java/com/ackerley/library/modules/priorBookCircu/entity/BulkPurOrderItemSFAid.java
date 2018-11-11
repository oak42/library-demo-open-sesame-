package com.ackerley.library.modules.priorBookCircu.entity;

import java.util.ArrayList;
import java.util.List;

/*
* 用于实现批量采购项目的 页面投放 与 入参绑定 的辅助类，内置list(所谓批量)，list item为 待列采购单的采购项对象，内有待收集的 quantity field...
*
* 尝试 直接用SimpleList<PBCPurOrderItem>... 失败...
* 貌似controller method parameter还是不能使用泛型(自认为反正泛型类型被擦除，然而框架支持就是没到位？)...
* 启用BulkPurOrderItemSFAid，好了...
* 【】后modeling改为直接在purOrder内部添一个purOrderItem的List...     so本construct可弃用...
*/
public class BulkPurOrderItemSFAid {
    private List<PBCPurOrderItem> list;

    public BulkPurOrderItemSFAid(){
        this.list = new ArrayList<>();
    }
    public BulkPurOrderItemSFAid(List<PBCPurOrderItem> list){
        this();
        this.list.addAll(list);
    }

    public List<PBCPurOrderItem> getList() {
        return list;
    }
    public void setList(List<PBCPurOrderItem> list) {
        this.list = list;
    }
}
