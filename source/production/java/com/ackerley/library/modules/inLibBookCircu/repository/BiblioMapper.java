package com.ackerley.library.modules.inLibBookCircu.repository;

import com.ackerley.library.common.persistence.CRUDMapper;
import com.ackerley.library.common.persistence.MyBatisMapper;
import com.ackerley.library.modules.inLibBookCircu.entity.Biblio;

import java.util.List;

@MyBatisMapper
public interface BiblioMapper extends CRUDMapper<Biblio>{
    List<Biblio> retrieveListFuzzyQuery(Biblio fiterBiblio);
}
