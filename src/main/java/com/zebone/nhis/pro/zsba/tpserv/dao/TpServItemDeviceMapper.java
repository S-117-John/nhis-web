package com.zebone.nhis.pro.zsba.tpserv.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.tpserv.vo.TpServItemDevice;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * TODO
 * @author 
 */
@Mapper
public interface TpServItemDeviceMapper{   
    /**
     * 根据主键查询
     */
    public TpServItemDevice getTpServItemDeviceById(@Param("pkItemDevice")String pkItemDevice); 

    /**
     * 查询出所有记录
     */
    public List<TpServItemDevice> findAllTpServItemDevice();    
    
    /**
     * 保存
     */
    public int saveTpServItemDevice(TpServItemDevice tpServItemDevice);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateTpServItemDevice(TpServItemDevice tpServItemDevice);
    
    /**
     * 根据主键删除
     */
    public int deleteTpServItemDevice(@Param("pkItemDevice")String pkItemDevice);
    
    public List<TpServItemDevice> getServItemDeviceList(TpServItemDevice master);
}

