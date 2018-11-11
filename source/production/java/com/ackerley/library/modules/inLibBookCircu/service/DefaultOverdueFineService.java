package com.ackerley.library.modules.inLibBookCircu.service;

import com.ackerley.library.common.service.DefaultCRUDService;
import com.ackerley.library.modules.inLibBookCircu.entity.OverdueFine;
import com.ackerley.library.modules.inLibBookCircu.repository.OverdueFineMapper;
import org.springframework.stereotype.Service;

@Service
public class DefaultOverdueFineService extends DefaultCRUDService<OverdueFineMapper, OverdueFine> implements OverdueFineService {
}
