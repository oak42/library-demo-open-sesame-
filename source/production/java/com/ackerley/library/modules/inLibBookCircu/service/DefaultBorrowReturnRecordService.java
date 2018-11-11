package com.ackerley.library.modules.inLibBookCircu.service;

import com.ackerley.library.common.service.DefaultCRUDService;
import com.ackerley.library.modules.inLibBookCircu.entity.BorrowReturnRecord;
import com.ackerley.library.modules.inLibBookCircu.repository.BorrowReturnRecordMapper;
import org.springframework.stereotype.Service;

@Service
public class DefaultBorrowReturnRecordService extends DefaultCRUDService<BorrowReturnRecordMapper, BorrowReturnRecord> implements BorrowReturnRecordService {
}
