package com.ackerley.library.modules.priorBookCircu.repository;

import com.ackerley.library.common.persistence.CRUDMapper;
import com.ackerley.library.common.persistence.MyBatisMapper;
import com.ackerley.library.modules.priorBookCircu.entity.PBCActnRecord;
import com.ackerley.library.modules.priorBookCircu.entity.PBCProcInstc;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisMapper
public interface ProcInstcMapper extends CRUDMapper<PBCProcInstc>{

    List<PBCProcInstc> retrievePositiveList(PBCProcInstc procInstc);    //

    List<PBCProcInstc> retrievePersonalList(@Param("procInstc") PBCProcInstc procInstc, @Param("actnRecord") PBCActnRecord actnRecord);

    Boolean isPersonalISBN13RcmdStillUnderReview(@Param("procInstc") PBCProcInstc procInstc, @Param("actnRecord") PBCActnRecord actnRecord); //用Boolean而非boolean，primitive type不可授null

    int contribute1Heat(PBCProcInstc procInstc);

    List<PBCProcInstc> retrieveToPurOrderList(PBCProcInstc procInstc);

/*
    int advanceStageByISBN13(String ISBN13, String fromStage, String toStage);
*/
    String retrieveStageAcqProcInstcIDByISBN13(PBCProcInstc procInstcWithISBN13);

    List<PBCProcInstc> retrieveToBeCatalogedList(PBCProcInstc procInstc);
}
