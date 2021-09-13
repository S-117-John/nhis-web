package com.zebone.nhis.cn.ipdw.dao;

import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 适应症
 */
@Mapper
public interface CnIndicationMapper {

    //根据患者主键查询对应的医嘱
    public List<Map<String,Object>> qryPvCnOrder(Map<String,Object> pam);
}
