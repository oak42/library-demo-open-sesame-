package com.ackerley.library.modules.sys.entity;

import com.ackerley.library.common.entity.BaseEntity;

public class SysRule extends BaseEntity{
    private String parmName;
    private String parmValue;   //【扣】统一用String类型表达、存放rule parameter value是否合适？先简化处理吧...



    public String getParmName() {
        return parmName;
    }

    public void setParmName(String parmName) {
        this.parmName = parmName;
    }

    public String getParmValue() {
        return parmValue;
    }

    public void setParmValue(String parmValue) {
        this.parmValue = parmValue;
    }
}
