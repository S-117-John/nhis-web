package com.zebone.nhis.webservice.dao;

import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Mapper
public interface LbPivasDrugMapper {

    /**
     * 查询待发药明细接口
     * @param paramMap
     * @return
     */
    public List<Map<String,Object>> qryPivaDrugDtInfo(Map<String,Object> paramMap);

    /**
     * 查询待退药明细接口
     * @param paramMap
     * @return
     */
    public List<Map<String,Object>> qryPivaRetDrugDtInfo(Map<String,Object> paramMap);
}
