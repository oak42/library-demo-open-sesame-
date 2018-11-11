package com.ackerley.library.modules.inLibBookCircu.repository;

import com.ackerley.library.common.persistence.CRUDMapper;
import com.ackerley.library.common.persistence.MyBatisMapper;
import com.ackerley.library.modules.inLibBookCircu.entity.InLibBook;

@MyBatisMapper
public interface InLibBookMapper extends CRUDMapper<InLibBook>{
    InLibBook retrieveOneByBarCode(InLibBook entityWithBarCode);
}
