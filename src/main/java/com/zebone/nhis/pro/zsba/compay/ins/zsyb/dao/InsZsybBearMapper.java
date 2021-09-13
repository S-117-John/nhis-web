package com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbBear;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * TODO
 * @author 
 */
 @Mapper
public interface InsZsybBearMapper{   
    /**
     * 根据主键查询
     */
    public InsZsBaYbBear getInsBearById(@Param("pkInsbear")String pkInsbear); 

    /**
     * 查询出所有记录
     */
    public List<InsZsBaYbBear> findAllInsBear();    
    
    /**
     * 保存
     */
    public int saveInsBear(InsZsBaYbBear insBear);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateInsBear(InsZsBaYbBear insBear);
    
    /**
     * 根据主键删除
     */
    public int deleteInsBear(@Param("pkInsbear")String pkInsbear);
    
    /**
     * 获取排胎时间
     * @param map
     * @return
     */
    public Map<String,Object> getWcFmrq(Map<String,Object> map);
}

