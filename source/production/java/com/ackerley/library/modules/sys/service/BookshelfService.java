package com.ackerley.library.modules.sys.service;

import com.ackerley.library.common.service.CRUDService;
import com.ackerley.library.modules.sys.entity.Bookshelf;
import com.ackerley.library.modules.sys.repository.BookshelfMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface BookshelfService extends CRUDService<BookshelfMapper, Bookshelf>{
    List<Bookshelf> retrieveList(Bookshelf filter, int page, int rows);
}
