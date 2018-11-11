package com.ackerley.library.modules.priorBookCircu.service;

import com.ackerley.library.common.service.DefaultCRUDService;
import com.ackerley.library.modules.priorBookCircu.entity.PBCProcInstc;
import com.ackerley.library.modules.priorBookCircu.repository.ProcInstcMapper;
import org.springframework.stereotype.Service;

@Service
public class DefaultProcInstcService extends DefaultCRUDService<ProcInstcMapper, PBCProcInstc> implements ProcInstcService{
}
