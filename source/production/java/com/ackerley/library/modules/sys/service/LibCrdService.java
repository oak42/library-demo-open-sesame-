package com.ackerley.library.modules.sys.service;

import com.ackerley.library.common.service.CRUDService;
import com.ackerley.library.modules.sys.entity.LibCrd;
import com.ackerley.library.modules.sys.repository.LibCrdMapper;

public interface LibCrdService extends CRUDService<LibCrdMapper, LibCrd>{

    LibCrd retrieveLibCrdWithLibCrdBarCode(String barCode);
}
