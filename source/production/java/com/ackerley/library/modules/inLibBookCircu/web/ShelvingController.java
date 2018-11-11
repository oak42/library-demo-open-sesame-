package com.ackerley.library.modules.inLibBookCircu.web;

import com.ackerley.library.common.web.BaseController;
import com.ackerley.library.modules.inLibBookCircu.entity.BulkBookShelvingAid;
import com.ackerley.library.modules.inLibBookCircu.entity.InLibBook;
import com.ackerley.library.modules.inLibBookCircu.service.IBCService;
import com.ackerley.library.modules.inLibBookCircu.service.InLibBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

/**
 * 只管简单的图书上架？后面可以把带拓展的  [更换 图书所属 书架定置定位(即更换InLibBook的bookshelf) 功能] 归到这里...
 */
@Controller
@RequestMapping("/inLibCircu/shelving")
public class ShelvingController extends BaseController{
    @Autowired
    InLibBookService bookService;
    @Autowired
    IBCService ibcService;

    @RequestMapping({"", "list"})
    public String toBeShelvedBookList(Model model) {
        BulkBookShelvingAid bulkBookShelvingAid = new BulkBookShelvingAid(ibcService.retrieveToBeShelvedBooks());
        model.addAttribute("bulkBookShelvingAid", bulkBookShelvingAid);
        return "modules/inLibBookCircu/shelving";
    }

    @RequestMapping({"doShelving"})
    public String doShelving(BulkBookShelvingAid bbsa, RedirectAttributes ras, @RequestParam Map<String, String> map) {
        addMessage(ras, "登记摆架图书(" + ibcService.doShelving(bbsa) + ")本...");
        return "redirect:/inLibCircu/shelving";
    }

}
