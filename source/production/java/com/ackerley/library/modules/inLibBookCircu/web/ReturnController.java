package com.ackerley.library.modules.inLibBookCircu.web;

import com.ackerley.library.common.web.BaseController;
import com.ackerley.library.modules.inLibBookCircu.entity.InLibBook;
import com.ackerley.library.modules.inLibBookCircu.service.IBCService;
import com.ackerley.library.modules.inLibBookCircu.service.InLibBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(path = "/inLibCircu/returnReg")
public class ReturnController extends BaseController {
    @Autowired
    IBCService ibcService;
    @Autowired
    InLibBookService inLibBookService;

    @RequestMapping(path = {"/{bookBarCode}", ""})
    public String returnBook(@PathVariable(value = "bookBarCode", required = false) String bookBarCode, Model model) {
        if(!StringUtils.isEmpty(bookBarCode)) {
            try {
                InLibBook book = inLibBookService.retrieveInLibBookWithInLibBookBarCode(bookBarCode);
                if (!book.getState().equals(InLibBook.ILBS_OUT)) {
                    addMessage(model, "错误：图书当前状态为非可还...可能①在架未借出；②待上架而未上架；③已登记遗失...");
                } else {
                    String feedback = ibcService.bookReturnReg(book.getID());
                    addMessage(model, feedback);
                }
            } catch (RuntimeException e) {
                addMessage(model, e.getMessage());
                e.printStackTrace();
            }
        }

        return "modules/inLibBookCircu/return";
    }
}