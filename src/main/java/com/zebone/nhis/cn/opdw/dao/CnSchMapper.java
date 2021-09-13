package com.zebone.nhis.cn.opdw.dao;

import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CnSchMapper {
    /**
     * 查询医生出诊信息
     * @param paramMap
     * @return
     */
    List<Map<String, Object>> qryClinicSchList(Map<String, Object> paramMap);

    /**
     * 查询医生出诊信息
     * @param paramMap
     * @return
     */
    List<Map<String, Object>> qryClinicSchListTic(Map<String, Object> paramMap);
}
