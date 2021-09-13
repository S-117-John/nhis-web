package com.zebone.nhis.common.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.emr.rec.rec.EmrMedDoc;
import com.zebone.nhis.common.module.emr.rec.rec.EmrMedRec;
import com.zebone.platform.modules.mybatis.Mapper;

import org.apache.ibatis.annotations.Param;

/**
 * 病历书写-病历书写-mapper
 * @author chengjia
 */
@Mapper
public interface EmrPubMapper{   
  
    /**
     * 查询患者病历记录
     * @param pkPv/typeCode/orderBy
     * @return
     */
    public List<EmrMedRec> queryPatMedRecDoc(Map paramMap);   
    
    /**
     * 根据主键查询病历文档内容
     */
    public EmrMedDoc getEmrMedDocById(@Param("pkDoc")String pkDoc); 

}
