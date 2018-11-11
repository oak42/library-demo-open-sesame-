package com.ackerley.library.modules.sys.service;

import com.ackerley.library.common.service.DefaultCRUDService;
import com.ackerley.library.modules.sys.entity.SysRule;
import com.ackerley.library.modules.sys.repository.SysRuleMapper;
import org.springframework.stereotype.Service;

@Service
public class DefaultSysRuleService extends DefaultCRUDService<SysRuleMapper, SysRule> implements SysRuleService{
    public int updateNonBlankByID(SysRule sysRule) {
        return mapper.updateNonBlankByID(sysRule);
    }
}
