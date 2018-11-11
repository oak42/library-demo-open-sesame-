package com.ackerley.library.modules.sys.service;

import com.ackerley.library.common.persistence.CRUDMapper;
import com.ackerley.library.common.service.CRUDService;
import com.ackerley.library.modules.sys.entity.User;
import com.ackerley.library.modules.sys.repository.UserMapper;

import java.util.List;

/**
 * Created by ackerley on 2018/5/5.
 */
public interface UserService extends CRUDService<UserMapper, User>{
    User getUserByLoginName(String loginName);
}
