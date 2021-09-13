package com.zebone.nhis.ma.pub.sd.dao;

import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CnAegerMapper {

    /**根据pkpv查询患者药品处方信息*/
    List<Map<String,Object>> qryOrdInfoByPkPv(Map<String,Object> paramMap);

}
