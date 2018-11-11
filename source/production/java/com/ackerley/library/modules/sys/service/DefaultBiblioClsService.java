package com.ackerley.library.modules.sys.service;

import com.ackerley.library.common.service.DefaultCRUDService;
import com.ackerley.library.modules.sys.entity.BiblioCls;
import com.ackerley.library.modules.sys.repository.BiblioClsMapper;
import org.springframework.stereotype.Service;

@Service
public class DefaultBiblioClsService extends DefaultCRUDService<BiblioClsMapper, BiblioCls> implements BiblioClsService{
}
