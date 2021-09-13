package com.zebone.nhis.pro.sd.cn.dao;

import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface RisLabMapper {

    public List<Map<String,Object>> qryPaitBs(Map<String,Object> paramMap);
}
