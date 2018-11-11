package com.ackerley.library.modules.priorBookCircu.web;

import com.ackerley.library.common.web.BaseController;
import com.ackerley.library.modules.priorBookCircu.entity.PBCProcInstc;
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
@RequestMapping(path = "/priorCircu/rcmd")
public class RcmdController extends BaseController{
    @Autowired
    private PBCService pbcs;

    @RequestMapping(path = {"", "list"})
    public String list(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "rows", required = false, defaultValue = "20") int rows, Model model){
        List<PBCProcInstc> rcmdList = pbcs.retrieveRcmdList(page, rows);
        model.addAttribute("pageInfo", new PageInfo<PBCProcInstc>(rcmdList));
        return "modules/priorBookCircu/acqRcmdList";
    }

    @RequestMapping(path = {"create", "update"})//暂时不写update逻辑了，采编建议录就录了，不提供改的逻辑...
    public String form(){   //无参，简化，暂不做后端校验不通过后的原form数据原样保留redirect了...
        return "modules/priorBookCircu/acqRcmdForm";
    }

    @RequestMapping(path = "save")
    public String save(PBCProcInstc rcmd, @RequestParam Map<String, String> paramMap, RedirectAttributes ras){
        StringBuilder sb = new StringBuilder().append("采购建议(ISBN13：").append(rcmd.getISBN13()).append("，标题：").append(rcmd.getTitle()).append(")");
        if(pbcs.recommendOne(rcmd) > 0) {
            sb.append("录入成功...");
        } else {
            sb.append("录入失败...").append("<br/>").append(rcmd.getRemarks()); //下层将失败原因写在remarks，层间调用参数乃in-out parameter...
        }
        addMessage(ras, sb.toString());

        if (paramMap.containsKey("continue")) {
            return "redirect:/priorCircu/rcmd/create";
        }
        if (paramMap.containsKey("list")) {
            return "redirect:/priorCircu/rcmd/list";
        }
        return "redirect:/priorCircu/rcmd/list";
    }



    //建议的删除逻辑？有必要么？
    //...
}
