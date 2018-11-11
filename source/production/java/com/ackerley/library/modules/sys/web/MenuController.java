package com.ackerley.library.modules.sys.web;

import com.ackerley.library.common.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by ackerley on 2018/5/9.
 */
@Controller
@RequestMapping(path = "/sys/menu")
public class MenuController extends BaseController{

    @RequestMapping(path = "tree")  //sysIndex.jsp就是用 data-href="${ctx}/sys/menu/tree?parentID=${_1stLevelMenu.id}" 来动态展现左侧次级菜单项的
    public String tree(){return "modules/sys/menu/menuTree";}
}
