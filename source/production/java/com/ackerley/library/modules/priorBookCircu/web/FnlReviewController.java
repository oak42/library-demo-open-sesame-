package com.ackerley.library.modules.priorBookCircu.web;

import com.ackerley.library.common.web.BaseController;
import com.ackerley.library.modules.priorBookCircu.entity.BulkAuditingSFAid;
import com.ackerley.library.modules.priorBookCircu.entity.PBCProcInstc;
import com.ackerley.library.modules.priorBookCircu.entity.ProcInstcAuditingPair;
import com.ackerley.library.modules.priorBookCircu.service.PBCService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/*
* 逻辑基本就是从PBCReviewController照抄来的...
*/
@Controller
@RequestMapping(path = "/priorCircu/fnlRvw")
public class FnlReviewController extends BaseController{
    @Autowired
    private PBCService pbcs;

    @RequestMapping(path = {"", "list"})
    public String list(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
                       @RequestParam(name = "rows", required = false, defaultValue = "5") int rows, Model model) {
        List<PBCProcInstc> list = pbcs.retrievePendingFnlReviewList(page, rows);
        BulkAuditingSFAid fnlReviewList = new BulkAuditingSFAid(list);
        model.addAttribute("fnlReviewList", fnlReviewList);
        model.addAttribute("pageInfo", new PageInfo<>(list));
        return "modules/priorBookCircu/rcmdFnlReviewList";
    }

    @RequestMapping(path = "submit")
    public String submit(BulkAuditingSFAid bulk, RedirectAttributes ras) {
        List<ProcInstcAuditingPair> list = bulk.getList();
        int count = pbcs.submitFnlReviewResults(list);
        addMessage(ras,"成功复审处理(" + count + ")本书的采编建议...");
        return "redirect:/priorCircu/fnlRvw/list";
    }

}
