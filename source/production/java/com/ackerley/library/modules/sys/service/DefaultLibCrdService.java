package com.ackerley.library.modules.sys.service;

import com.ackerley.library.common.service.DefaultCRUDService;
import com.ackerley.library.modules.sys.entity.LibCrd;
import com.ackerley.library.modules.sys.repository.LibCrdMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultLibCrdService extends DefaultCRUDService<LibCrdMapper, LibCrd> implements LibCrdService {

    //凭bar code获取lib card...
    public LibCrd retrieveLibCrdWithLibCrdBarCode(String barCode) {

        LibCrd filterBarCode = new LibCrd();
        filterBarCode.setBarCode(barCode);
        List<LibCrd> list = retrieveList(filterBarCode);

        if(list.size() == 0) {
            throw new RuntimeException("错误：借书证条码 不存在 或 已作废...");
        } else if (list.size() > 1) {
            throw new RuntimeException("系统内部逻辑错误：一个条码只能对应一个实体...");
        }

        return list.get(0);   //当前lib crd...
    }

}
