package com.ackerley.library.modules.sys.repository;

import com.ackerley.library.common.persistence.CRUDMapper;
import com.ackerley.library.common.persistence.MyBatisMapper;
import com.ackerley.library.modules.sys.entity.Menu;

import java.util.List;

/**
 * Created by ackerley on 2018/5/9.
 */
@MyBatisMapper
public interface MenuMapper extends CRUDMapper<Menu> {
    List<Menu> getMenuListByUserID(String userID);
}
