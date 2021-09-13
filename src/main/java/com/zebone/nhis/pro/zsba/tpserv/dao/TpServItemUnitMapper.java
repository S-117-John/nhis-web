package com.zebone.nhis.pro.zsba.tpserv.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.tpserv.vo.TpServItemUnit;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * TODO
 * @author 
 */
@Mapper
public interface TpServItemUnitMapper{   
    /**
     * 根据主键查询
     */
    public TpServItemUnit getTpServItemUnitById(@Param("pkItemUnit")String pkItemUnit); 

    /**
     * 查询出所有记录
     */
    public List<TpServItemUnit> findAllTpServItemUnit();    
    
    /**
     * 保存
     */
    public int saveTpServItemUnit(TpServItemUnit tpServItemUnit);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateTpServItemUnit(TpServItemUnit tpServItemUnit);
    
    /**
     * 根据主键删除
     */
    public int deleteTpServItemUnit(@Param("pkItemUnit")String pkItemUnit);
    
    public List<TpServItemUnit> getServItemUnitList(TpServItemUnit master);
}

