package com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbTsbzdj;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * TODO
 * @author 
 */
 @Mapper
public interface InsZsybTsbzdjMapper{   
    /**
     * 根据主键查询
     */
    public InsZsBaYbTsbzdj getInsZsybTsbzdjById(@Param("pkInszsybtsbzdj")String pkInszsybtsbzdj); 

    /**
     * 查询出所有记录
     */
    public List<InsZsBaYbTsbzdj> findAllInsZsybTsbzdj();    
    
    /**
     * 保存
     */
    public int saveInsZsybTsbzdj(InsZsBaYbTsbzdj insZsybTsbzdj);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateInsZsybTsbzdj(InsZsBaYbTsbzdj insZsybTsbzdj);
    
    /**
     * 根据主键删除
     */
    public int deleteInsZsybTsbzdj(@Param("pkInszsybtsbzdj")String pkInszsybtsbzdj);
    
    /**
     * 获取明细
     */
    public List<InsZsBaYbTsbzdj> getYbMx(Map<String,Object> map);
    
    /**
     * 删除明细
     */
    public void delYbMx(Map<String,Object> map);
}

