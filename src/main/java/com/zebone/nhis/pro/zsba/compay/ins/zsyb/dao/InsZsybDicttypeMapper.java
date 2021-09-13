package com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbDicttype;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * TODO
 * @author 
 */
 @Mapper
public interface InsZsybDicttypeMapper{   
    /**
     * 根据主键查询
     */
    public InsZsBaYbDicttype getInsDicttypeById(@Param("pkInsdicttype")String pkInsdicttype); 

    /**
     * 查询出所有记录
     */
    public List<InsZsBaYbDicttype> findAllInsDicttype();    
    
    /**
     * 保存
     */
    public int saveInsDicttype(InsZsBaYbDicttype insDicttype);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateInsDicttype(InsZsBaYbDicttype insDicttype);
    
    /**
     * 根据主键删除
     */
    public int deleteInsDicttype(@Param("pkInsdicttype")String pkInsdicttype);
}

