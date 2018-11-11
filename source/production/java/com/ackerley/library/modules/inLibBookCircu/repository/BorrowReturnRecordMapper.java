package com.ackerley.library.modules.inLibBookCircu.repository;

import com.ackerley.library.common.persistence.CRUDMapper;
import com.ackerley.library.common.persistence.MyBatisMapper;
import com.ackerley.library.modules.inLibBookCircu.entity.BorrowReturnRecord;

@MyBatisMapper
public interface BorrowReturnRecordMapper extends CRUDMapper<BorrowReturnRecord>{
}
