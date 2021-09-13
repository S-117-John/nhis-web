package com.zebone.nhis.pro.zsba.tpserv.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.tpserv.vo.TpServItem;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * TODO
 * @author 
 */
@Mapper
public interface TpServItemMapper{   
    /**
     * 根据主键查询
     */
    public TpServItem getTpServItemById(@Param("pkItem")String pkItem); 

    /**
     * 查询出所有记录
     */
    public List<TpServItem> findAllTpServItem();    
    
    /**
     * 保存
     */
    public int saveTpServItem(TpServItem tpServItem);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateTpServItem(TpServItem tpServItem);
    
    /**
     * 根据主键删除
     */
    public int deleteTpServItem(@Param("pkItem")String pkItem);
    
    public List<TpServItem> getServItemList(TpServItem master);
}

