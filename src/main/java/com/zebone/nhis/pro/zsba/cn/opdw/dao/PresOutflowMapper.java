package com.zebone.nhis.pro.zsba.cn.opdw.dao;

import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PresOutflowMapper {

    /**
     * 查询是否有不存在外流药品的处方-药
     * @param paramMap
     * @return
     */
    Integer qryNotExists(Map<String,Object> paramMap);

    /**
     * 查询处方-药品信息
     * @param paramMap
     * @return
     */
    List<Map<String,Object>> qryPresBd(Map<String,Object> paramMap);
}
