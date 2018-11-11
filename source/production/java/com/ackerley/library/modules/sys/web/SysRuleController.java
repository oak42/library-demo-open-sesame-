package com.ackerley.library.modules.sys.web;

import com.ackerley.library.common.web.BaseController;
import com.ackerley.library.modules.sys.entity.SysRule;
import com.ackerley.library.modules.sys.service.SysRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(path = "/sys/rules")
public class SysRuleController extends BaseController{
    @Autowired
    private SysRuleService srs;

    @RequestMapping(path = {"", "index"})
    public String index(SysRule sr, Model model) {
        if(!StringUtils.isEmpty(sr.getID())) {  //保存单个sys rule parameter value修改...
            SysRule sysRuleOriginal = srs.retrieveOne(sr);
            if (srs.updateNonBlankByID(sr) == 1) {
                String str = new StringBuilder()
                        .append("参数 [")
                        .append(sysRuleOriginal.getParmName())
                        .append("] 修改成功 (")
                        .append(sysRuleOriginal.getParmValue())
                        .append(" → ")
                        .append(sr.getParmValue())
                        .append(")")
                        .toString();
                addMessage(model, str);
            }
        }

        List<SysRule> sysRuleList = srs.retrieveList(new SysRule());
        model.addAttribute("sysRuleList", sysRuleList);
        return "modules/sys/rulesAndRegulations/sysRulesIndex";
    }
}
