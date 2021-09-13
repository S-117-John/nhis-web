package com.zebone.nhis.cn.pub.dao;

import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ClinicalMapper {

    /**
     * 查询患者检验信息
     * @param paramMap
     * @return
     */
    public List<Map<String,Object>> qryLab(Map<String, Object> paramMap);

    /**
     * 查询患者检验信息
     * @param paramMap
     * @return
     */
    public List<Map<String,Object>> qryLabReport(Map<String, Object> paramMap);

    /**
     * 查询患者检查信息
     * @param paramMap
     * @return
     */
    public List<Map<String,Object>> qryRis(Map<String, Object> paramMap);
}
