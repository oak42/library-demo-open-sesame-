package com.ackerley.library.modules.inLibBookCircu.web;

import com.ackerley.library.common.web.BaseController;
import com.ackerley.library.modules.inLibBookCircu.service.InLibBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/inLibCircu/InLibBook")
public class InLibBookController extends BaseController{
    @Autowired
    private InLibBookService ilbs;

    @RequestMapping("")
    @ResponseBody
    public Object inLibBookInfoQuery(@RequestParam String ID) {
        return ilbs.retrieveOne(ID);
    }
}
