package com.zebone.nhis.webservice.pskq.dao;

import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CostDetailInpatDao {

    List<Map<String,Object>> billCode(Map<String,Object> param);
}
