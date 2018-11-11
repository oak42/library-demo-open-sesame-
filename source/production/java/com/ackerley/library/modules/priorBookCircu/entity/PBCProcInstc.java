package com.ackerley.library.modules.priorBookCircu.entity;

import com.ackerley.library.common.entity.BaseEntity;
import com.ackerley.library.common.entity.FieldRangeFilter;
import com.ackerley.library.modules.sys.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;
import java.util.List;

/*
* 入库前流转的流程实例：(简单，完全线性，没有分支...当前关卡通过就推向下一阶段，不通过的话stage就到此为止...)
* ①(流程发起)用户提采编建议；②审核员审核采编建议；③(流程结束)复审员终审；
* 采编建议发起后，stage为prior_circu_stg_RVW，即审核中...
* 审核员审核后，stage为prior_circu_stg_FNL_RVW，即复审中...
* 复审员审核后，stage为prior_circu_stg_FINISHED，即完结...
* ★ stage是状态；动作是行为，可看做stage的"中间状态"，推动stage在状态间的转变...
* 前一stage的值可作为prior_circu_action_record记录表的action值的prefix，如PCS_ini:PCA_initiate,PCS_review:PCA_approve,PCS_review:PCA_reject...
*
*/
public class PBCProcInstc extends BaseEntity{
    @JsonProperty(value="ISBN13")
    private String ISBN13;  //
    private String title;   //

    //流程实例 当前状态/阶段 (所谓当前，是指待实施的阶段，所以prior_circu_stg_INI永远不会出现在stage，但可能是PBCActnRecord的action的前缀)
    private String stage;
    public static final String PCS_INI = "prior_circu_stg_initiate";                        //虽然永远不会出现在proc_instc表的stage字段，但会作为前缀出现在actn_record表的action字段
    public static final String PCS_RVW = "prior_circu_stg_review";                          //待审核(初审)
    public static final String PCS_RVW_RJCT = "prior_circu_stg_review_rejected";            //审核(初审)不通过
    public static final String PCS_FNL_RVW = "prior_circu_stg_final_review";                //待终审
    public static final String PCS_FNL_RVW_RJCT = "prior_circu_stg_final_review_rejected";  //终审不通过
    public static final String PCS_ACQ = "prior_circu_stg_acquiring";                       //终审通过后待采编入库(过程中)
    public static final String PCS_CTLG = "prior_circu_stg_cataloging";                     //已采购入库验收，待编目
    public static final String PCS_IN_LIB = "prior_circu_stg_in_lib";                       //已编目入库了，待上架

    private int heat;   //热度值，一条ISBN13审核流程周期内(未到FINISHED)的所有各个用户对本书的推荐使其+1，即周期内所有对本书的action_record记录proc_id都是关联此proc_instc的id...

    private List<PBCActnRecord> actnRecordList;

    public PBCProcInstc(){}

    public String getISBN13() {
        return ISBN13;
    }
    public void setISBN13(String ISBN13) {
        this.ISBN13 = ISBN13;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getStage() {
        return stage;
    }
    public void setStage(String stage) {
        this.stage = stage;
    }
    public int getHeat() {
        return heat;
    }
    public void setHeat(int heat) {
        this.heat = heat;
    }

    public List<PBCActnRecord> getActnRecordList() {
        return actnRecordList;
    }
    public void setActnRecordList(List<PBCActnRecord> actnRecordList) {
        this.actnRecordList = actnRecordList;
    }



}
