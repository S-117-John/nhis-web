package com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbIcd;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * TODO
 * @author 
 */
 @Mapper
public interface InsZsybIcdMapper{   
    /**
     * 根据主键查询
     */
    public InsZsBaYbIcd getInsZsybIcdById(@Param("pkInszsybicd")String pkInszsybicd); 

    /**
     * 查询出所有记录
     */
    public List<InsZsBaYbIcd> findAllInsZsybIcd();    
    
    /**
     * 保存
     */
    public int saveInsZsybIcd(InsZsBaYbIcd insZsybIcd);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateInsZsybIcd(InsZsBaYbIcd insZsybIcd);
    
    /**
     * 根据主键删除
     */
    public int deleteInsZsybIcd(@Param("pkInszsybicd")String pkInszsybicd);
}

