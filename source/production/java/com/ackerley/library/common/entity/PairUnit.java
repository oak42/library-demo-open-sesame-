package com.ackerley.library.common.entity;

/*【猜】
* 想直接用泛型类型供spring做data binding，貌似不行...
* request parameter能收上来，object graph也对，因为能成功铺出...
* 估计spring在对收上来的数据进行组装时对泛型的类型参数无法决定...
* 导致有Object类型的对象，但无fileds...
*/
public class PairUnit<TT, RR> {
    private TT subject;
    private RR result;

    public PairUnit() {}    //若无，框架无法使用无参构造器造bean，报错类似Could not instantiate property type [com.ackerley.library.common.entity.PairUnit] to auto-grow nested property path...
    public PairUnit(TT subject) {
        this.subject = subject;
    }

    public TT getSubject() {
        return subject;
    }
    public void setSubject(TT subject) {
        this.subject = subject;
    }
    public RR getResult() {
        return result;
    }
    public void setResult(RR result) {
        this.result = result;
    }
}
