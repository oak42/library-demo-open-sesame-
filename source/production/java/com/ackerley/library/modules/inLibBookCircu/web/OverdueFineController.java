package com.ackerley.library.modules.inLibBookCircu.web;

import com.ackerley.library.common.web.BaseController;
import com.ackerley.library.modules.inLibBookCircu.service.OverdueFineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/inLibCircu/overdueFine")
public class OverdueFineController extends BaseController{
    @Autowired
    OverdueFineService overdueFineService;

    @RequestMapping("")
    @ResponseBody
    public Object overdueFineInfoQuery(@RequestParam("ID") String ID) {
        return overdueFineService.retrieveOne(ID);
    }
}
