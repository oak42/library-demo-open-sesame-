package com.ackerley.library.modules.priorBookCircu.service;

import com.ackerley.library.common.service.DefaultCRUDService;
import com.ackerley.library.modules.priorBookCircu.entity.PBCActnRecord;
import com.ackerley.library.modules.priorBookCircu.repository.ActnRecordMapper;
import org.springframework.stereotype.Service;

@Service
public class DefaultActnRecordService extends DefaultCRUDService<ActnRecordMapper, PBCActnRecord> implements ActnRecordService{

}
