package com.ackerley.library.modules.inLibBookCircu.service;

import com.ackerley.library.common.service.DefaultCRUDService;
import com.ackerley.library.modules.inLibBookCircu.entity.Biblio;
import com.ackerley.library.modules.inLibBookCircu.repository.BiblioMapper;
import org.springframework.stereotype.Service;

@Service
public class DefaultBiblioService extends DefaultCRUDService<BiblioMapper, Biblio> implements BiblioService {
}
