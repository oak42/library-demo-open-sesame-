package com.ackerley.library.modules.inLibBookCircu.web;

import com.ackerley.library.common.web.BaseController;
import com.ackerley.library.modules.inLibBookCircu.service.BorrowReturnRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/inLibCircu/borrowNRetrunRecord")
public class BorrowAndReturnRecordController extends BaseController {
    @Autowired
    private BorrowReturnRecordService brrs;

    @RequestMapping("")
    @ResponseBody
    public Object recordInfoQuery(@RequestParam("ID") String ID) {
        return brrs.retrieveOne(ID);
    }
}
