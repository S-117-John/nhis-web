package com.zebone.nhis.pro.zsrm.cn.dao;

import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CnOpDiagMapper {

    List<Map<String,Object>> qryDaigByPk(Map<String,Object> paramMap);
    List<Map<String,Object>> qryDaigAtt(Map<String,Object> paramMap);
}

