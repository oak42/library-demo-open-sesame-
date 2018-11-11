package com.ackerley.library.modules.priorBookCircu.web;

import com.ackerley.library.common.entity.SimpleList;
import com.ackerley.library.common.web.BaseController;
import com.ackerley.library.modules.priorBookCircu.entity.BulkPurOrderItemSFAid;
import com.ackerley.library.modules.priorBookCircu.entity.PBCProcInstc;
import com.ackerley.library.modules.priorBookCircu.entity.PBCPurOrder;
import com.ackerley.library.modules.priorBookCircu.entity.PBCPurOrderItem;
import com.ackerley.library.modules.priorBookCircu.service.PBCService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "/priorCircu/purOrder")
public class PurOrderController extends BaseController{
    @Autowired
    PBCService pbcs;

    @RequestMapping(path = {"", "list"})
    public String list(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
                       @RequestParam(name = "rows", required = false, defaultValue = "5") int rows, Model model) {
        List<PBCPurOrder> list = pbcs.retrievePurOrderList(page, rows);
        model.addAttribute("pageInfo", new PageInfo<>(list));
        return "modules/priorBookCircu/purOrderIndex";
    }

    @RequestMapping(path = "create")    /* 没"update"，暂不提供 采购单的update逻辑 */
    public String form(Model model) {
        List<PBCPurOrderItem> toPurOrderItemList = pbcs.retrieveToPurOrderItemList();
        model.addAttribute("toPurOrderItemList", toPurOrderItemList);
        return "modules/priorBookCircu/purOrderGeneration";
    }

    @RequestMapping(path = "save")
    public String save(PBCPurOrder purOrder, RedirectAttributes ras, @RequestParam Map<String, String> params) {
        int count = pbcs.createPurOrder(purOrder);
        addMessage(ras, "成功保存采购单\"" + purOrder.getName() + "\"，共(" + count + ")项书目...");
        return "redirect:/priorCircu/purOrder";
    }

    @RequestMapping(path = "delete")    /* 采购单的delete逻辑："done"的采购单不许delete，只许delete "on"的，前端页面也保证了...*/
    public String delete(Model model) {
        //todo...
        return "redirect:/priorCircu/purOrder";
    }

    @RequestMapping(path = "items")    /* 查看采购单明细项。点击采购单，查看采购单明细(items，适合打印：抬头，内容明细项，脚注) */
    public String items(@RequestParam(name = "ID") String ID, Model model) {
        PBCPurOrder purOrder = pbcs.retrievePurOrderWithItemsByID(ID);
        model.addAttribute("purOrder", purOrder);
        return "modules/priorBookCircu/purOrderItemsView";
    }

    @RequestMapping(path = "inboundReview")/* 入库验收。点击'on'状态(尚未验收入库的)采购单，填写其中各item的验收数量*/
    public String inboundReview(PBCPurOrder purOrder, Model model, @RequestParam Map<String, String> params) {
        //若为POST提交...   验收页面inboundReview中 设hidden的'orderState' input field(value正好从'acquiring'推进为'inLib')以 和GET 区分...
        String orderState = purOrder.getOrderState();
        if(!StringUtils.isEmpty(orderState) && orderState.equals("inLib")) {
            pbcs.inboundReview(purOrder);
            return "redirect:/priorCircu/purOrder";
        }
        //若为GET...
        purOrder = pbcs.retrievePurOrderWithItemsByID(purOrder.getID());
        model.addAttribute("purOrder", purOrder);
        return "modules/priorBookCircu/purOrderInboundReview";
    }




}
