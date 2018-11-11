package com.ackerley.library.modules.inLibBookCircu.service;

import com.ackerley.library.common.service.CRUDService;
import com.ackerley.library.modules.inLibBookCircu.entity.InLibBook;
import com.ackerley.library.modules.inLibBookCircu.repository.InLibBookMapper;

public interface InLibBookService extends CRUDService<InLibBookMapper, InLibBook> {
    InLibBook retrieveInLibBookWithInLibBookBarCode(String barCode);
}
