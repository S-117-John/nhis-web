package com.zebone.nhis.base.bd.dao;

import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ParamMapper {

    List<Map<String,Object>> getParamList(@Param("params")Map<String,Object> map);
}
