package com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbLog;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * TODO
 * @author 
 */
 @Mapper
public interface InsZsybLogMapper{   
    /**
     * 根据主键查询
     */
    public InsZsBaYbLog getInsLogById(@Param("pkInslog")String pkInslog); 

    /**
     * 查询出所有记录
     */
    public List<InsZsBaYbLog> findAllInsLog();    
    
    /**
     * 保存
     */
    public int saveInsLog(InsZsBaYbLog insLog);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateInsLog(InsZsBaYbLog insLog);
    
    /**
     * 根据主键删除
     */
    public int deleteInsLog(@Param("pkInslog")String pkInslog);
}

