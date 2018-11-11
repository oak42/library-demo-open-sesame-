package com.ackerley.library.modules.sys.web;

import com.ackerley.library.common.web.BaseController;
import com.ackerley.library.modules.sys.entity.User;
import com.ackerley.library.modules.sys.service.UserService;
import com.ackerley.library.modules.sys.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by ackerley on 2018/5/5.
 */
@Controller
@RequestMapping(path = "/sys/user")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;

    @RequestMapping(path = "")
    public String get(@RequestParam("id") String id, Model model) {
        User user = userService.retrieveOne(id);
        model.addAttribute("user", user);
        model.addAttribute("menulist", UserUtil.getMenuList());
        return "justForTest7777777";
    }

    @RequestMapping(path = "create")
    public String create(User user) {
        if(user.getRealName() != null){     //【待】肯定不是这么简单的校验逻辑，以后加后端统一校验手段
            userService.saveOne(user);
        }

        return "";
    }

    @RequestMapping(path = "update")
    public String update(User user) {   //由于saveOne是通过是否新record来分别是insert还是update，所以页面form应当有<form:hidden path="id"/>
        if(user.getRealName() != null){     //【待】肯定不是这么简单的校验逻辑，以后加后端统一校验手段
            userService.saveOne(user);
        }
        return "";
    }


}
