package com.ackerley.library.modules.sys.service;

import com.ackerley.library.common.service.CRUDService;
import com.ackerley.library.modules.sys.entity.SysRule;
import com.ackerley.library.modules.sys.repository.SysRuleMapper;

public interface SysRuleService extends CRUDService<SysRuleMapper, SysRule>{
    int updateNonBlankByID(SysRule sysRule);
}
