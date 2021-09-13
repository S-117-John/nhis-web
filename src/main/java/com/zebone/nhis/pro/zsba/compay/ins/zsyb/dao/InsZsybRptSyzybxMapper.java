package com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbRptSyzybx;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * TODO
 * @author 
 */
 @Mapper
public interface InsZsybRptSyzybxMapper{   
    /**
     * 根据主键查询
     */
    public InsZsBaYbRptSyzybx getInsZsybRptSyzybxById(@Param("pkInsacct")String pkInsacct); 

    /**
     * 查询出所有记录
     */
    public List<InsZsBaYbRptSyzybx> findAllInsZsybRptSyzybx();    
    
    /**
     * 保存
     */
    public int saveInsZsybRptSyzybx(InsZsBaYbRptSyzybx insZsybRptSyzybx);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateInsZsybRptSyzybx(InsZsBaYbRptSyzybx insZsybRptSyzybx);
    
    /**
     * 根据主键删除
     */
    public int deleteInsZsybRptSyzybx(@Param("pkInsacct")String pkInsacct);
    
    /**
     * 获取明细
     */
    public List<InsZsBaYbRptSyzybx> getYbMx(Map<String,Object> map);
    
    /**
     * 删除明细
     */
    public void delYbMx(Map<String,Object> map);
}

