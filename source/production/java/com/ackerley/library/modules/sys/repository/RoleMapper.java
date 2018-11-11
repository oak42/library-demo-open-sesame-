package com.ackerley.library.modules.sys.repository;

import com.ackerley.library.common.persistence.CRUDMapper;
import com.ackerley.library.common.persistence.MyBatisMapper;
import com.ackerley.library.modules.sys.entity.Role;

import java.util.List;

/**
 * Created by ackerley on 2018/7/7.
 */
@MyBatisMapper
public interface RoleMapper extends CRUDMapper<Role>{
    List<Role> retrieveListByUserID(String userID);
}
