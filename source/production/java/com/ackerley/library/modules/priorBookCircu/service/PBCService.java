package com.ackerley.library.modules.priorBookCircu.service;

import com.ackerley.library.modules.priorBookCircu.entity.PBCProcInstc;
import com.ackerley.library.modules.priorBookCircu.entity.PBCPurOrder;
import com.ackerley.library.modules.priorBookCircu.entity.PBCPurOrderItem;
import com.ackerley.library.modules.priorBookCircu.entity.ProcInstcAuditingPair;

import java.util.List;

public interface PBCService {

    int recommendOne(PBCProcInstc procInstc);   //试提交一则建议...

    List<PBCProcInstc> retrieveRcmdList(int page, int rows);

    List<PBCProcInstc> retrievePendingReviewList(int page, int rows);

    int submitReviewResults(List<ProcInstcAuditingPair> list);

    List<PBCProcInstc> retrievePendingFnlReviewList(int page, int rows);

    int submitFnlReviewResults(List<ProcInstcAuditingPair> list);

    List<PBCPurOrder> retrievePurOrderList(int page, int rows);

    List<PBCPurOrderItem> retrieveToPurOrderItemList();

    int createPurOrder(PBCPurOrder purOrder);   //返回的是purchase order关联的items条数...

    PBCPurOrder retrievePurOrderWithItemsByID(String purOrderID);    //

    int inboundReview(PBCPurOrder purOrder);

    List<PBCProcInstc> retrieveToBeCatalogedList(int page, int rows);

    int cataloging(PBCProcInstc procInstc);

    int getInboundNbr(String ISBN13);
}
