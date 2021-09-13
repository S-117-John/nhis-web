package com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbDict;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * TODO
 * @author 
 */
 @Mapper
public interface InsZsybDictMapper{   
    /**
     * 根据主键查询
     */
    public InsZsBaYbDict getInsDictById(@Param("pkInsdict")String pkInsdict); 

    /**
     * 查询出所有记录
     */
    public List<InsZsBaYbDict> findAllInsDict();    
    
    /**
     * 保存
     */
    public int saveInsDict(InsZsBaYbDict insDict);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateInsDict(InsZsBaYbDict insDict);
    
    /**
     * 根据主键删除
     */
    public int deleteInsDict(@Param("pkInsdict")String pkInsdict);
}

