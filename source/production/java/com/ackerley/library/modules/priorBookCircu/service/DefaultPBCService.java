package com.ackerley.library.modules.priorBookCircu.service;

import com.ackerley.library.modules.priorBookCircu.entity.*;
import com.ackerley.library.modules.priorBookCircu.repository.ProcInstcMapper;
import com.ackerley.library.modules.priorBookCircu.repository.PurOrderItemMapper;
import com.ackerley.library.modules.priorBookCircu.repository.PurOrderMapper;
import com.ackerley.library.modules.sys.utils.UserUtil;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultPBCService implements PBCService {
    @Autowired
    private ProcInstcMapper procInstcMapper;
    @Autowired
    private ProcInstcService procInstcService;
    @Autowired
    private ActnRecordService actnRecordService;

    @Autowired
    private PurOrderMapper purOrderMapper;
    @Autowired
    private PurOrderItemMapper purOrderItemMapper;
    @Autowired
    private PurOrderService purOrderService;
    @Autowired
    private PurOrderItemService purOrderItemService;
/*
    private ActnRecordMapper actnRecordMapper;
*/

//建议■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    //添加一条采编建议
    @Transactional
    public int recommendOne(PBCProcInstc procInstc) {
        String route = preCheck(procInstc);

        String userID = UserUtil.getCurrentUser().getID();
        Timestamp now = new Timestamp(System.currentTimeMillis());

        switch (route) {
            case "initiate_new_proc_instc":
                procInstc.setStage(PBCProcInstc.PCS_RVW); //用户建议行为将state推向下一state(INI state永远不会出现在DB的state字段)...
                procInstcService.saveOne(procInstc);      //新纪录在DefaultCRUDService层面save前会生成、加上ID...
                PBCActnRecord actnRecord = new PBCActnRecord(procInstc.getID(), PBCProcInstc.PCS_INI + ":" + PBCActnRecord.PCA_INI, now, userID);
                return actnRecordService.saveOne(actnRecord);
                //return语句后，此处break无意义，省略...
            case "contribute_heat":
                procInstcMapper.contribute1Heat(procInstc);
                PBCActnRecord actnRecordA = new PBCActnRecord(procInstc.getID(), PBCProcInstc.PCS_INI + ":" + PBCActnRecord.PCA_INI, now, userID);  //这里的procInstc的ID是preCheck时附上的
                return actnRecordService.saveOne(actnRecordA);
            case "unsuccessful_recommendation":
                return 0;//不大于0 对controller层表示建议录入失败...
        }
        return 0;//其实逻辑上可以不用写这句的？【扣】但语法上编译器提示没有return statement...
    }
    //recommend之前先检查 是否已经在库，是否已经终审通过：只要有一如是，false，告知用户无需再recommend，并在remarks写上反馈message；
    //                                               若皆非，返回true，表示事前检查通过...
    // (但有个问题：若preCheck的时候还未终审通过，preCheck后recommendOne前终审通过，略尴尬。
    //  考虑不在controller中调用preCheck，而是在recommendOne中调用preCheck，作为一个@Transactional整体？)
    private String preCheck(PBCProcInstc procInstc){    //【扣】preCheck时，procInstc应(待加校验？)已有实际值的field：isbn13、title(就是spring MVC bind进来的相关field)，以便retrievePositiveList凭相关约束正常工作...
        List<PBCProcInstc> list = procInstcMapper.retrievePositiveList(procInstc);
        if(list.isEmpty()) {
            return "initiate_new_proc_instc";
        } else {
            String stage = list.get(0).getStage();  //【注意！】可能出现list有length，而list element全为null reference的情况！是否若mapper file中resultmap没配置好的话，导致resultset不能转化为java type？
            switch (stage) {
                //该ISBN13还在审核中，进一步区分本人是否已经建议过该ISBN13...
                case PBCProcInstc.PCS_RVW:
                case PBCProcInstc.PCS_FNL_RVW:
                    PBCActnRecord actnRecord = new PBCActnRecord();
                    actnRecord.setDoerID(UserUtil.getCurrentUser().getID());
                    Boolean b = procInstcMapper.isPersonalISBN13RcmdStillUnderReview(procInstc, actnRecord);
                    if (b == null) {//本人还未对本ISBN13建议过，则contribute heat...
                        procInstc.setID(list.get(0).getID());   //省的在上层再去取一遍ID，action record需要此设为procID... procInstc既是入参又是出参哦...
                        return "contribute_heat";
                    } else {        //本人已经建议过...不重复刷heat...
                        procInstc.setRemarks("您提交过的对该书的采编建议还在审核中，请耐心等待...");
                        return "unsuccessful_recommendation";
                    }
                case PBCProcInstc.PCS_ACQ:
                    procInstc.setRemarks("已经在采购中，敬请耐心等候入库...");
                    return "unsuccessful_recommendation";
                case PBCProcInstc.PCS_CTLG:
                    procInstc.setRemarks("已经在编目中，敬请耐心等候上架...");
                    return "unsuccessful_recommendation";
                case PBCProcInstc.PCS_IN_LIB:
                    procInstc.setRemarks("已经在库，您可择机借阅...");
                    return "unsuccessful_recommendation";
            }
        }
        return "";//其实逻辑上可以不用写这句的？【扣】但语法上编译器提示没有return statement...
    }

    //获取本人的参与的建议列表，包括各种stage的，包括rejected的...(带action record list)
    @Transactional
    public List<PBCProcInstc> retrieveRcmdList(int page, int rows){
        PBCProcInstc filterProcInstc = new PBCProcInstc();
        PBCActnRecord filterActnRecord = new PBCActnRecord();
        filterActnRecord.setDoerID(UserUtil.getCurrentUser().getID());
        filterActnRecord.setAction(PBCProcInstc.PCS_INI + ":" + PBCActnRecord.PCA_INI);
        PageHelper.startPage(page, rows);                       // .countColumn("pi.id");  orz...直接耦合了mapper file层面了...
        List<PBCProcInstc> list = procInstcMapper.retrievePersonalList(filterProcInstc, filterActnRecord);
        filterActnRecord.setAction(null);                       //
        for (PBCProcInstc procInstc : list) {
            filterActnRecord.setProcID(procInstc.getID());
            procInstc.setActnRecordList(actnRecordService.retrieveList(filterActnRecord));
        }
        return list;
    }



//审核■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    //获取待审核记录列表(就是按stage为PBCProcInstc.PCS_RVW去找proc_instc)
    @Transactional
    public List<PBCProcInstc> retrievePendingReviewList(int page, int rows) {
        PBCProcInstc filter = new PBCProcInstc();
        filter.setStage(PBCProcInstc.PCS_RVW);
        PageHelper.startPage(page, rows);
        return procInstcMapper.retrieveList(filter);
    }

    //存储审核结果，返回审核(显式表决，明确点选approve或reject)条数count...
    @Transactional
    public int submitReviewResults(List<ProcInstcAuditingPair> list) {
        String userID = UserUtil.getCurrentUser().getID();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        int count = 0;
        for (ProcInstcAuditingPair pair : list) {
            String result = pair.getResult();
            PBCProcInstc procInstc = pair.getSubject();
            if (result != null && !result.equals("") && result.equals("reject")) {  // 逻辑运算的短路求值，不会对null String操作...
                procInstc.setStage(PBCProcInstc.PCS_RVW_RJCT);
                procInstcMapper.updateNonBlankByID(procInstc);
                PBCActnRecord actnRecord = new PBCActnRecord(procInstc.getID(), PBCProcInstc.PCS_RVW + ":" + PBCActnRecord.PCA_RJCT, now, userID);
                if (actnRecordService.saveOne(actnRecord) > 0) {
                    count++;
                }
            }
            if (result != null && !result.equals("") && result.equals("approve")) {  // 逻辑运算的短路求值，不会对null String操作...
                procInstc.setStage(PBCProcInstc.PCS_FNL_RVW);
                procInstcMapper.updateNonBlankByID(procInstc);
                PBCActnRecord actnRecord = new PBCActnRecord(procInstc.getID(), PBCProcInstc.PCS_RVW + ":" + PBCActnRecord.PCA_APRV, now, userID);
                if (actnRecordService.saveOne(actnRecord) > 0) {
                    count++;
                }
            }
        }
        return count;
    }

//终审■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    //获取待终审记录列表
    @Transactional
    public List<PBCProcInstc> retrievePendingFnlReviewList(int page, int rows) {
        PBCProcInstc filter = new PBCProcInstc();
        filter.setStage(PBCProcInstc.PCS_FNL_RVW);
        PageHelper.startPage(page, rows);
        return procInstcMapper.retrieveList(filter);
    }

    @Transactional
    public int submitFnlReviewResults(List<ProcInstcAuditingPair> list) {
        String userID = UserUtil.getCurrentUser().getID();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        int count = 0;
        for (ProcInstcAuditingPair pair : list) {
            String result = pair.getResult();
            PBCProcInstc procInstc = pair.getSubject();
            if (result != null && !result.equals("") && result.equals("reject")) {
                procInstc.setStage(PBCProcInstc.PCS_FNL_RVW_RJCT);
                procInstcMapper.updateNonBlankByID(procInstc);
                PBCActnRecord actnRecord = new PBCActnRecord(procInstc.getID(), PBCProcInstc.PCS_FNL_RVW + ":" + PBCActnRecord.PCA_RJCT, now, userID);
                if (actnRecordService.saveOne(actnRecord) > 0) {
                    count++;
                }
            }
            if (result != null && !result.equals("") && result.equals("approve")) {  // 逻辑运算的短路求值，不会对null String操作...
                procInstc.setStage(PBCProcInstc.PCS_ACQ);
                procInstcMapper.updateNonBlankByID(procInstc);
                PBCActnRecord actnRecord = new PBCActnRecord(procInstc.getID(), PBCProcInstc.PCS_FNL_RVW + ":" + PBCActnRecord.PCA_APRV, now, userID);
                if (actnRecordService.saveOne(actnRecord) > 0) {
                    count++;
                }
            }
        }
        return count;
    }

//采购单■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    //获取采购单列表
    @Transactional
    public List<PBCPurOrder> retrievePurOrderList(int page, int rows) {
        PBCPurOrder dummy = new PBCPurOrder();
        PageHelper.startPage(page, rows);
        return purOrderService.retrieveList(dummy);
    }

    //待列采购单(项)列表 = 所有终审通过项 - 所有在"on"状态的现有采购单们下的items的总和
    @Transactional
    public List<PBCPurOrderItem> retrieveToPurOrderItemList() {
        PBCProcInstc dummy = new PBCProcInstc();
        List<PBCProcInstc> procInstcList =  procInstcMapper.retrieveToPurOrderList(dummy);
        ArrayList<PBCPurOrderItem> toPurOrderItemlist = new ArrayList<>();
        for (PBCProcInstc procInstc : procInstcList) {
            toPurOrderItemlist.add(new PBCPurOrderItem(procInstc));
        }
        return toPurOrderItemlist;
    }

    //创建(保存) 采购单 及 关联的明细列表(items)
    @Transactional
    public int createPurOrder(PBCPurOrder purOrder) {
        String userID = UserUtil.getCurrentUser().getID();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        purOrder.setCreatorID(userID);
        purOrder.setCreationTime(now);
        purOrder.setOrderState("acquiring");
        purOrderService.saveOne(purOrder);
        String purOrderID = purOrder.getID();               //saveOne后，ID就有了...
        List<PBCPurOrderItem> itemlist = purOrder.getList();
        int count = 0;      //代表item条数...
        for (PBCPurOrderItem item : itemlist) {
            if(!StringUtils.isEmpty(item.getISBN13())) {    //
                item.setPurOrderID(purOrderID);
                purOrderItemService.saveOne(item);
                count++;
            }
        }
        return count;
    }

    //获取一个采购单(带其下关联的items)
    @Transactional
    public PBCPurOrder retrievePurOrderWithItemsByID(String purOrderID) {
        return purOrderMapper.retrieveOneWithItemsByID(purOrderID);
    }

    //保存(更新)采购单验收信息inboundQty
    @Transactional
    public int inboundReview(PBCPurOrder purOrder) {
        String userID = UserUtil.getCurrentUser().getID();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        purOrder.setInboundReviewerID(userID);
        purOrder.setInboundTime(now);
//        purOrder.setOrderState("inLib");     //已在页面设了hidden的，controller入参绑定时就有了...
        purOrderMapper.updateNonBlankByID(purOrder);
        List<PBCPurOrderItem> itemlist = purOrder.getList();
        int count = 0;      //代表实际验收的item条数...
        for (PBCPurOrderItem item : itemlist) {
            if(item.getInboundQty() > 0) {    //这样实际验收数为0的proc instc还是有机会下轮采购单候选...
                purOrderItemMapper.updateNonBlankByID(item);    //写入验收数

                PBCProcInstc procInstc = new PBCProcInstc();
                procInstc.setISBN13(item.getISBN13());
                String procInstcID = procInstcMapper.retrieveStageAcqProcInstcIDByISBN13(procInstc);

                procInstc.setID(procInstcID);
                procInstc.setISBN13(null);  //
                procInstc.setStage(PBCProcInstc.PCS_CTLG);
                procInstcMapper.updateNonBlankByID(procInstc);

                PBCActnRecord actnRecord = new PBCActnRecord(procInstcID, PBCProcInstc.PCS_ACQ + ":" + PBCActnRecord.PCA_INBOUND, now, userID);
                actnRecordService.saveOne(actnRecord);

                count++;
            }
        }
        return count;
    }

//编目■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    //编目(cataloging)虽然划分为inLib book circu，但 在prior book circu中 留有部分逻辑 需要完成，封在PBCService...
    @Transactional
    public List<PBCProcInstc> retrieveToBeCatalogedList(int page, int rows) {
        PBCProcInstc dummy = new PBCProcInstc();
        PageHelper.startPage(page, rows);
        return procInstcMapper.retrieveToBeCatalogedList(dummy);
    }

    @Transactional
    public int cataloging(PBCProcInstc procInstc) { //procInstc 中 须有 ID；当然此场景中也应有ISBN13，用于IBCService中的cataloging逻辑...
        procInstc.setStage(PBCProcInstc.PCS_IN_LIB);
        procInstcMapper.updateNonBlankByID(procInstc);

        String userID = UserUtil.getCurrentUser().getID();
        Timestamp now = new Timestamp(System.currentTimeMillis());

        PBCActnRecord actnRecord = new PBCActnRecord(procInstc.getID(), PBCProcInstc.PCS_CTLG + ":" + PBCActnRecord.PCA_DONE, now, userID);
        return actnRecordService.saveOne(actnRecord);
    }

    //获取item的入库数，此种modeling有问题的...仅适用于一个ISBN13采购一次的情况...(因此在前台页面也设置了quantity 输入框，editable；这里的返回值仅作为quantity的初始值，可人为修改...)
    // 其实周全一点的做法是：
    // 自然承接采购入库审核流程阶段，以pur_order为编目选择入口，
    // 在pur_order表新添一个中间state：cataloging，其下所有pur_order_item都被cataloged时推进到in_lib...(相应地，pur_order_item表添上is_cataloged字段、cataloging_transactor...)
    @Transactional
    public int getInboundNbr(String ISBN13) {
        Integer nbr = purOrderItemMapper.retrieveInboundQtyByISBN13(ISBN13);
        if(nbr != null) {
            return nbr;
        }

        return 0;
    }

}
