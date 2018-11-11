package com.ackerley.library.modules.inLibBookCircu.service;

import com.ackerley.library.common.service.DefaultCRUDService;
import com.ackerley.library.modules.inLibBookCircu.entity.InLibBook;
import com.ackerley.library.modules.inLibBookCircu.repository.InLibBookMapper;
import com.ackerley.library.modules.sys.entity.LibCrd;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultInLibBookService extends DefaultCRUDService<InLibBookMapper, InLibBook> implements InLibBookService {

    //凭bar code获取inLib book...
    public InLibBook retrieveInLibBookWithInLibBookBarCode(String barCode) {

        InLibBook filterBarCode = new InLibBook();
        filterBarCode.setBarCode(barCode);
        List<InLibBook> list = retrieveList(filterBarCode);

        if(list.size() == 0) {
            throw new RuntimeException("错误：图书条码 不存在 或 已作废...");
        } else if (list.size() > 1) {
            throw new RuntimeException("系统内部逻辑错误：一个条码只能对应一个实体...");
        }

        return list.get(0);   //当前lib crd...
    }

}
