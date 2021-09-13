package com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbStItemcate;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * TODO
 * @author 
 */
 @Mapper
public interface InsZsybStItemcateMapper{   
    /**
     * 根据主键查询
     */
    public InsZsBaYbStItemcate getInsStItemcateById(@Param("pkInsitemcate")String pkInsitemcate); 

    /**
     * 查询出所有记录
     */
    public List<InsZsBaYbStItemcate> findAllInsStItemcate();    
    
    /**
     * 保存
     */
    public int saveInsStItemcate(InsZsBaYbStItemcate insStItemcate);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateInsStItemcate(InsZsBaYbStItemcate insStItemcate);
    
    /**
     * 根据主键删除
     */
    public int deleteInsStItemcate(@Param("pkInsitemcate")String pkInsitemcate);
}

