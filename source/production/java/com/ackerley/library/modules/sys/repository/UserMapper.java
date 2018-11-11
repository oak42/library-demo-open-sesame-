package com.ackerley.library.modules.sys.repository;

import com.ackerley.library.common.persistence.CRUDMapper;
import com.ackerley.library.common.persistence.MyBatisMapper;
import com.ackerley.library.modules.sys.entity.User;

import java.util.List;

/**
 * Created by ackerley on 2018/5/5.
 */
@MyBatisMapper
public interface UserMapper extends CRUDMapper<User>{

}
