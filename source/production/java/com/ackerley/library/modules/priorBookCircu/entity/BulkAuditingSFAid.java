package com.ackerley.library.modules.priorBookCircu.entity;

import java.util.ArrayList;
import java.util.List;

/*
public class BulkAuditingSFAid extends PairList<PBCProcInstc, String>{
    public BulkAuditingSFAid(){
        super();
    }
    public BulkAuditingSFAid(List<PBCProcInstc> list){
        super(list);
    }
}
*/

/*
* 用于实现批量审核的 页面投放 与 入参绑定 的辅助类，内置list(所谓批量)，list item为一个pair构造：一个待审核对象 搭一个 对应的审核结果对象
*/
//若如上，写成泛型形式，spring mvc能投放，但回收后无法组装，可能是其data binding对泛型的支持还不到位？
//先写成hardcoded看看，不用继承(擦除)泛型的形式，但ProcInstcAuditingPair仍是继承(擦除)泛型...结果可行！
// 推出？ → spring MVC 对于controller in parameter 的 class 有限制，不能 泛型内套泛型 ？
public class BulkAuditingSFAid {
    private List<ProcInstcAuditingPair> list;

    public BulkAuditingSFAid(){
        this.list = new ArrayList<>();
    }

    public BulkAuditingSFAid(List<PBCProcInstc> list){
        this();
        for (PBCProcInstc i : list) {
            this.list.add(new ProcInstcAuditingPair(i));
        }
    }

    public List<ProcInstcAuditingPair> getList() {
        return list;
    }
    public void setList(List<ProcInstcAuditingPair> list) {
        this.list = list;
    }
}

