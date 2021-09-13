package com.zebone.nhis.pro.zsba.compay.pub.dao;

import java.util.List;
import java.util.Map;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * TODO
 * @author 
 */
 @Mapper
public interface PubInsSettleMapper{   
    
    /** 根据住院就诊主键获取住院收费明细
     * @param map
     * @return
     */
    public List<Map<String,Object>> getChargeDetails(Map<String,Object> map);
  
}

