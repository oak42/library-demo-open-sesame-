package com.ackerley.library.modules.priorBookCircu.service;

import com.ackerley.library.common.service.DefaultCRUDService;
import com.ackerley.library.modules.priorBookCircu.entity.PBCPurOrder;
import com.ackerley.library.modules.priorBookCircu.repository.PurOrderMapper;
import org.springframework.stereotype.Service;

@Service
public class DefaultPurOrderService extends DefaultCRUDService<PurOrderMapper, PBCPurOrder> implements PurOrderService{
}
