package com.ackerley.library.common.entity;

import java.util.ArrayList;
import java.util.List;

/*
* 在内部放个集合，便于Spring MVC对 批量的 entity、相关paring entity(如每个entity对应审核state) 进行data binding，又不想直接在entity内加上存放相关state的field？
* 就将二者打包到一个PairUnit，其List集合封为PairList的一个field...
* 不知道是否有必要这样？
*
* T - 主体类型
* R - 辅助对象()类型
* P - 迫不得已？？？so ugly？？？
*/
public class PairList<T, S> {
    private List<PairUnit<T, S>> list;

    public PairList() {
        list = new ArrayList<PairUnit<T, S>>();
    }

    public PairList(List<T> subjectList) {
        this();
        for (T i : subjectList) {
            list.add(new PairUnit<T, S>(i));
        }
    }

    public List<PairUnit<T, S>> getList() {
        return list;
    }
    public void setList(List<PairUnit<T, S>> list) {
        this.list = list;
    }


/*
    * 私用 内部类
    * 一个pair单元...
    * 改了，各种问题，不知道是不是由于PairUnit是内部类导致的，先写成外部的看看...
    */
/*

    private class PairUnit<TT, RR> {
        private TT subject;
        private RR result;

        private PairUnit(TT subject) {
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
*/

}
