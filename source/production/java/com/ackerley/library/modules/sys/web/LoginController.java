package com.ackerley.library.modules.sys.web;

import com.ackerley.library.common.web.BaseController;
import com.ackerley.library.modules.sys.security.Principal;
import com.ackerley.library.modules.sys.utils.SecurityUtil;
import com.ackerley.library.modules.sys.utils.UserUtil;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Created by ackerley on 2018/5/10.
 * 具体流转逻辑参考jeesite的LoginController...
 */
@Controller
public class LoginController extends BaseController{

/*
    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String login(){
        //shiro逻辑待补，这里①直接pass掉(true)登录过程(包括shiro filter过程)；②直接pass掉身份判断，导向sys身份的index page...
        if(true){
            return "redirect:/index";
        }
        return "/login";  //整个shiro登录-鉴权都还没写...
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String failedLogin(){
        return "/login";
    }
*/


    @RequestMapping(value = "/loginForm", method = RequestMethod.GET)
    public String loginForm() {
        Principal principal = UserUtil.getCurrentPrimaryPrincipal();
        //【】若已登录，再次访问登入入口，则跳转首页...
        if(principal != null) {
            return "redirect:/index";
        } else {
            return "modules/sys/loginForm";     // 【扣】为何调试时初次启动会入两次这里？之前也有印象好像在调试时controller的有些方法会入两次，不是modelattribute哦！
        }

    }

    /*
     * 真正登录的POST请求由Filter验证，验证成功则导向successUrl，失败的话...
     */
    @RequestMapping(value = "/loginForm", method = RequestMethod.POST)
    public String loginFailure(Model model, HttpServletRequest req) {
        Principal principal = UserUtil.getCurrentPrimaryPrincipal();
        //【】若已登录，再次访问登入入口，则跳转首页...
        if(principal != null) {
            return "redirect:/index";
        } else {
            addMessage(model, "登陆名不存在 或 密码错误，请重试...");
            return "modules/sys/loginForm";
        }

    }


    @RequestMapping(path = {"/index", ""})
    public String sysIndex(){

        return "modules/sys/sysIndex";
    }

}
