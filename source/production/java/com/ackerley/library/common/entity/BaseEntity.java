package com.ackerley.library.common.entity;

import com.ackerley.library.common.utils.IDGeneration.IDGenerator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.StringUtils;

/**
 * Created by ackerley on 2018/5/16.
 * 模仿jeesite的DataEntity+BaseEntity...
 */
public class BaseEntity {
    @JsonProperty(value="ID")
    protected String ID;
    protected String remarks;
    protected String delFlag;
    public static final String DEL_FLAG_RETAINED = "0";
    public static final String DEL_FLAG_DELETED = "1";

    public String getID() {
        return ID;
    }
    public void setID(String ID) {
        this.ID = ID;
    }

    public String getRemarks() {
        return remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDelFlag() {
        return delFlag;
    }
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    protected boolean isNewRecord = false;
    //【扣】spring的bean概念对于getter、setter的命名是有规范的？intellij(对于boolean型field)自动生成的getter、setter不满足spring的要求！
    public boolean getIsNewRecord() {
        return isNewRecord || StringUtils.isEmpty(ID);  //【鸣】CRUDService的save方法中，区别 是insert还是update 的关键...
    }
    public void setIsNewRecord(boolean newRecord) {   //需显式手动set；在service中save...时，将凭此isNewRecord区别对待是update还是insert...
        isNewRecord = newRecord;
    }

    public void preCreate(){
        if(getIsNewRecord()){
            setID(IDGenerator.nextId());
        }
    }

    public void preUpdate(){

    }

}
