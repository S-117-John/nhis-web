package com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbInfo;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * TODO
 * @author 
 */
 @Mapper
public interface InsZsybInfoMapper{   
    /**
     * 根据主键查询
     */
    public InsZsBaYbInfo getInsInfoById(@Param("pkInsinfo")String pkInsinfo); 

    /**
     * 查询出所有记录
     */
    public List<InsZsBaYbInfo> findAllInsInfo();    
    
    /**
     * 保存
     */
    public int saveInsInfo(InsZsBaYbInfo insInfo);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateInsInfo(InsZsBaYbInfo insInfo);
    
    /**
     * 根据主键删除
     */
    public int deleteInsInfo(@Param("pkInsinfo")String pkInsinfo);
}

