package com.ackerley.library.modules.priorBookCircu.repository;

import com.ackerley.library.common.persistence.CRUDMapper;
import com.ackerley.library.common.persistence.MyBatisMapper;
import com.ackerley.library.modules.priorBookCircu.entity.PBCPurOrderItem;

@MyBatisMapper
public interface PurOrderItemMapper extends CRUDMapper<PBCPurOrderItem>{
    Integer retrieveInboundQtyByISBN13(String ISBN13);
}
