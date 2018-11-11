package com.ackerley.library.modules.priorBookCircu.entity;

import com.ackerley.library.common.entity.PairUnit;

public class ProcInstcAuditingPair extends PairUnit<PBCProcInstc, String>{
    public ProcInstcAuditingPair(){
        super();
    }                               //多余
    public ProcInstcAuditingPair(PBCProcInstc procInstc){
        super(procInstc);
    }//多余
}