package com.ackerley.library.modules.sys.service;

import com.ackerley.library.common.service.DefaultCRUDService;
import com.ackerley.library.modules.sys.entity.Bookshelf;
import com.ackerley.library.modules.sys.repository.BookshelfMapper;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultBookshelfService extends DefaultCRUDService<BookshelfMapper, Bookshelf> implements BookshelfService {
    public List<Bookshelf> retrieveList(Bookshelf filter, int page, int rows) {
        PageHelper.startPage(page, rows);
        return mapper.retrieveList(filter);
    }

}
