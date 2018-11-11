package com.ackerley.library.modules.sys.utils;

import com.ackerley.library.common.utils.SpringAppCtxtUtil;
import com.ackerley.library.modules.sys.entity.Menu;
import com.ackerley.library.modules.sys.entity.User;
import com.ackerley.library.modules.sys.repository.MenuMapper;
import com.ackerley.library.modules.sys.security.Principal;
import com.ackerley.library.modules.sys.service.UserService;
import org.apache.shiro.SecurityUtils;

import java.util.List;

public class UserUtil {
    private static UserService userService = SpringAppCtxtUtil.getBean(UserService.class);
    private static MenuMapper menuMapper = SpringAppCtxtUtil.getBean(MenuMapper.class);

    //获取 当前登陆用户的 所有accessible的menus...
    public static List<Menu> getMenuList(){
        //这里需要用user ID作为argument来获取menu list...然而shiro认证-鉴权系统还未上，先hardcode下了...
        return menuMapper.getMenuListByUserID(getCurrentUser().getID());

    }

    //shiro principal
    public static Principal getCurrentPrimaryPrincipal() {
        return (Principal)SecurityUtils.getSubject().getPrincipal();
        //DelegatingSubject.getPrincipal()
        // - Returns this Subject's application-wide uniquely identifying principal,
        // or null if this Subject is anonymous because it doesn't yet have any associated account data (for example, if they haven't logged in).
    }

    //获取 当前登陆用户...shiro...
    public static User getCurrentUser(){
        User user = null;

        //本app目前除了静态资源，shiro filter chain决定了没有anonymous能操作的场景，所以在getCurrentPrimaryPrincipal获取Principal后就无需判null处理了吧？保险起见多写个判断吧...
        Principal principal = getCurrentPrimaryPrincipal();
        if (principal == null) {
            throw new RuntimeException("当前用户并未登陆！你是怎么进来的？");
        } else {
            user = userService.retrieveOne(principal.getUserID());
        }

        if (user == null) {
            throw new RuntimeException("系统错误！shiro realm认证时能获取该user的，认证成功后再次获取该user就 没了没了没了？");
        }

        return user;
    }

    //

}
