package com.ackerley.library.modules.priorBookCircu.service;

import com.ackerley.library.common.service.DefaultCRUDService;
import com.ackerley.library.modules.priorBookCircu.entity.PBCPurOrderItem;
import com.ackerley.library.modules.priorBookCircu.repository.PurOrderItemMapper;
import org.springframework.stereotype.Service;

@Service
public class DefaultPurOrderItemService extends DefaultCRUDService<PurOrderItemMapper, PBCPurOrderItem> implements PurOrderItemService{
}
