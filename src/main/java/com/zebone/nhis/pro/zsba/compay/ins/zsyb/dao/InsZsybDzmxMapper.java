package com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbDzmx;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * TODO
 * @author 
 */
 @Mapper
public interface InsZsybDzmxMapper{   
    /**
     * 根据主键查询
     */
    public InsZsBaYbDzmx getInsZsybDzmxById(@Param("pkInszsybdzmx")String pkInszsybdzmx); 

    /**
     * 查询出所有记录
     */
    public List<InsZsBaYbDzmx> findAllInsZsybDzmx();    
    
    /**
     * 保存
     */
    public int saveInsZsybDzmx(InsZsBaYbDzmx insZsybDzmx);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateInsZsybDzmx(InsZsBaYbDzmx insZsybDzmx);
    
    /**
     * 根据主键删除
     */
    public int deleteInsZsybDzmx(@Param("pkInszsybdzmx")String pkInszsybdzmx);
}

