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
import java.util.Map;

@Controller
@RequestMapping(path = "/priorCircu/rvw")
public class ReviewController extends BaseController{
    @Autowired
    private PBCService pbcs;

    @RequestMapping(path = {"", "list"})
    public String list(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
                       @RequestParam(name = "rows", required = false, defaultValue = "5") int rows, Model model) {
        List<PBCProcInstc> list = pbcs.retrievePendingReviewList(page, rows);
        BulkAuditingSFAid reviewList = new BulkAuditingSFAid(list);
        model.addAttribute("reviewList", reviewList);
        model.addAttribute("pageInfo", new PageInfo<>(list));
        return "modules/priorBookCircu/rcmdReviewList";
    }

    @RequestMapping(path = "submit")
    public String submit(BulkAuditingSFAid bulk, RedirectAttributes ras, @RequestParam Map<String, String> map) {
        List<ProcInstcAuditingPair> list = bulk.getList();
        int count = pbcs.submitReviewResults(list);
        addMessage(ras,"成功审核处理(" + count + ")本书的采编建议...");
        return "redirect:/priorCircu/rvw/list";
    }


//下面这段也行？(再试一试)，证明了BulkAuditingFormObject没有必要(上面这种通过继承泛型时指定类型参数的方式擦除类型参数 与 此种直接在入参声明时指定类型参数    都行)！！！！！！！
//又写了个可以一直蛇咬尾式的一环套一环的小例子，证明data binding对于收集上来的 表单数据的对象图的点的个数、即层数 是没有限制的、无限的。既然如此，问题在哪儿呢？
/*
@RequestMapping(path = {"", "list"})
public String list(Model model) {
    List<PBCProcInstc> list = pbcs.retrievePendingReviewList();
    PairList<PBCProcInstc, String> reviewList = new PairList<>(list);
    model.addAttribute("reviewList", reviewList);
    return "modules/priorBookCircu/rcmdReviewList";
}

    @RequestMapping(path = "submit")
    public String submit(PairList<PBCProcInstc, String> bulk, @RequestParam Map<String, String> map) {

        return "";
    }
*/




}
