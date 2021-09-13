package com.zebone.nhis.base.drg.dao;

import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;
@Mapper
public interface BdTermCchiMapper {
    //查询CCHI
    public List<Map<String, Object>> qryTermCchi(Map map);
}
