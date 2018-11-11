package com.ackerley.library.modules.sys.service;

import com.ackerley.library.common.persistence.CRUDMapper;
import com.ackerley.library.common.service.CRUDService;
import com.ackerley.library.common.service.DefaultCRUDService;
import com.ackerley.library.modules.sys.entity.User;
import com.ackerley.library.modules.sys.repository.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultUserService extends DefaultCRUDService<UserMapper, User> implements UserService{
    public User getUserByLoginName(String loginName) {
        User filter = new User();
        filter.setLoginName(loginName);
        List<User> userList = mapper.retrieveList(filter);

        int listSize = userList.size();
        if(listSize == 0) {
            return null;
        } else if(listSize > 1) {
            throw new RuntimeException("系统错误，存在同loginName的user！");
        }
        return userList.get(0); //正常情况只应有一个...
    }
}
