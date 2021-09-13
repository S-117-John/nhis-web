package com.zebone.nhis.pro.zsba.tpserv.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.tpserv.vo.TpServItemType;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * TODO
 * @author 
 */
@Mapper
public interface TpServItemTypeMapper{   
    /**
     * 根据主键查询
     */
    public TpServItemType getTpServItemTypeById(@Param("pkItemType")String pkItemType); 

    /**
     * 查询出所有记录
     */
    public List<TpServItemType> findAllTpServItemType();    
    
    /**
     * 保存
     */
    public int saveTpServItemType(TpServItemType tpServItemType);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateTpServItemType(TpServItemType tpServItemType);
    
    /**
     * 根据主键删除
     */
    public int deleteTpServItemType(@Param("pkItemType")String pkItemType);
    
    public List<TpServItemType> getServItemTypeList(TpServItemType master);
}

