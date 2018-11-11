package com.ackerley.library.modules.sys.security;

import com.ackerley.library.modules.sys.entity.User;

/**
 * jeesite是将Principal写成realm的静态内部类，我就放在外面，看到后面有啥影响...
 */
public class Principal {


    private String userID;
    private String userRealName;
    private String userLoginName;

    public Principal(User user) {
        this.userID = user.getID();
        this.userRealName = user.getRealName();
        this.userLoginName = user.getLoginName();
    }

    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }
    public String getUserRealName() {
        return userRealName;
    }
    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }
    public String getUserLoginName() {
        return userLoginName;
    }
    public void setUserLoginName(String userLoginName) {
        this.userLoginName = userLoginName;
    }
}
