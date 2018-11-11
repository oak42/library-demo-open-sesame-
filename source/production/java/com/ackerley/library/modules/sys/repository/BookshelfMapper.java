package com.ackerley.library.modules.sys.repository;

import com.ackerley.library.common.persistence.CRUDMapper;
import com.ackerley.library.common.persistence.MyBatisMapper;
import com.ackerley.library.modules.sys.entity.Bookshelf;

import java.util.List;

/**
 * Created by ackerley on 2018/5/16.
 */
@MyBatisMapper
public interface BookshelfMapper extends CRUDMapper<Bookshelf>{

}
