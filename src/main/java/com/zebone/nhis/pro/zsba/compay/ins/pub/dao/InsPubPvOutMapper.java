package com.zebone.nhis.pro.zsba.compay.ins.pub.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.compay.ins.pub.vo.InsZsPubPvOut;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * TODO
 * @author 
 */
 @Mapper
public interface InsPubPvOutMapper{   
    /**
     * 根据主键查询
     */
    public InsZsPubPvOut getInsPvOutById(@Param("pkInspvout")String pkInspvout); 

    /**
     * 查询出所有记录
     */
    public List<InsZsPubPvOut> findAllInsPvOut();    
    
    /**
     * 保存
     */
    public int saveInsPvOut(InsZsPubPvOut insPvOut);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateInsPvOut(InsZsPubPvOut insPvOut);
    
    /**
     * 根据主键删除
     */
    public int deleteInsPvOut(@Param("pkInspvout")String pkInspvout);
    
    /** 根据住院就诊主键和时间段获取住院收费明细 - 项目
     * @param map
     * @return
     */
    public List<Map<String,Object>> getChargeDetails(Map<String,Object> map);
    
    /** 根据住院就诊主键和时间段获取住院收费明细 - 药品
     * @param map
     * @return
     */
    public List<Map<String,Object>> getChargeDetailsToYp(Map<String,Object> map);
    
    /** 根据住院就诊主键获取异地医保出院登记界面初始数据
     * @param map
     * @return
     */
   public List<Map<String,Object>> getDischargeRegistrationData(Map<String,Object> map);
}

