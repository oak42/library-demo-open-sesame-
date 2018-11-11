package com.ackerley.library.modules.sys.repository;

import com.ackerley.library.common.persistence.CRUDMapper;
import com.ackerley.library.common.persistence.MyBatisMapper;
import com.ackerley.library.modules.sys.entity.BiblioCls;
import com.ackerley.library.modules.sys.entity.SysRule;

@MyBatisMapper
public interface SysRuleMapper extends CRUDMapper<SysRule>{
}
