package com.ackerley.library.modules.priorBookCircu.repository;

import com.ackerley.library.common.persistence.CRUDMapper;
import com.ackerley.library.common.persistence.MyBatisMapper;
import com.ackerley.library.modules.priorBookCircu.entity.PBCPurOrder;

@MyBatisMapper
public interface PurOrderMapper extends CRUDMapper<PBCPurOrder>{
    //带上关联的purchase order items...('list' field)
    PBCPurOrder retrieveOneWithItemsByID(String ID);
    PBCPurOrder retrieveOneWithItemsByID(PBCPurOrder purOrderWithID);
}
