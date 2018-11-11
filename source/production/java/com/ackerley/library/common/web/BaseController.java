package com.ackerley.library.common.web;

import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


public class BaseController {
    //添加model消息
    protected void addMessage(Model model, String... messages){
        StringBuilder sb = new StringBuilder();
        int length = messages.length;
        for(int i = 0; i < length; i++){
            sb.append(i > 0 ? "<br/>" : "").append(messages[i]);
        }
        model.addAttribute("message", sb.toString());
    }

    //添加flash消息
    protected void addMessage(RedirectAttributes rdrctAttr, String... messages){
        StringBuilder sb = new StringBuilder();
        int length = messages.length;
        for(int i = 0; i < length; i++){
            sb.append(i > 0 ? "<br/>" : "").append(messages[i]);
        }
        rdrctAttr.addFlashAttribute("message", sb.toString());
    }

}
