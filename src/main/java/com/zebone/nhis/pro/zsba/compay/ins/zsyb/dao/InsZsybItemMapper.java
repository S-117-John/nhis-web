package com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbItem;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * TODO
 * @author 
 */
 @Mapper
public interface InsZsybItemMapper{   
    /**
     * 根据主键查询
     */
    public InsZsBaYbItem getInsItemById(@Param("pkInsitem")String pkInsitem); 

    /**
     * 查询出所有记录
     */
    public List<InsZsBaYbItem> findAllInsItem();    
    
    /**
     * 保存
     */
    public int saveInsItem(InsZsBaYbItem insItem);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateInsItem(InsZsBaYbItem insItem);
    
    /**
     * 根据主键删除
     */
    public int deleteInsItem(@Param("pkInsitem")String pkInsitem);
}

