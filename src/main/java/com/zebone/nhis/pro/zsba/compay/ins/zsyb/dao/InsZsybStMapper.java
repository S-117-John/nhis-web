package com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbSt;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * TODO
 * @author 
 */
 @Mapper
public interface InsZsybStMapper{   
    /**
     * 根据主键查询
     */
    public InsZsBaYbSt getInsStById(@Param("pkInsst")String pkInspv); 

    /**
     * 查询出所有记录
     */
    public List<InsZsBaYbSt> findAllInsSt();    
    
    /**
     * 保存
     */
    public int saveInsSt(InsZsBaYbSt insSt);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateInsSt(InsZsBaYbSt insSt);
    
    /**
     * 根据主键删除
     */
    public int deleteInsSt(@Param("pkInsst")String pkInspv);
    
    
    /** 根据住院就诊主键获取住院收费明细-项目
     * @param map
     * @return
     */
    public List<Map<String,Object>> getChargeDetails(Map<String,Object> map);

    /** 用于修改医保上传明细
     * @param map
     * @return
     */
    public List<Map<String,Object>> getChargeDetailsUpdate(Map<String,Object> map);

    
    /** 根据住院就诊主键获取住院收费明细-药品
     * @param map
     * @return
     */
    public List<Map<String,Object>> getChargeDetailsYp(Map<String,Object> map);
    
    /** 用于修改医保上传明细
     * @param map
     * @return
     */
    public List<Map<String,Object>> getChargeDetailsYpUpdate(Map<String,Object> map);
    
    /** 根据住院就诊主键获取住院收费明细B表
     * @param map
     * @return
     */
    public List<Map<String,Object>> getChargeDetailsB(Map<String,Object> map);
    
    
    public List<InsZsBaYbSt> getInsStList(Map<String,Object> map);
    
    /** 根据结算主键获取住院收费明细，对账时重新结算用的-项目
     * @param map
     * @return
     */
    public List<Map<String,Object>> getChargeDetails2(Map<String,Object> map);
    
    /** 根据结算主键获取住院收费明细，对账时重新结算用的-药品
     * @param map
     * @return
     */
    public List<Map<String,Object>> getChargeDetailsYp2(Map<String,Object> map);
}

