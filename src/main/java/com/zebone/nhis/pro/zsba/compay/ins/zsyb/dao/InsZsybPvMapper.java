package com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbPv;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * TODO
 * @author 
 */
 @Mapper
public interface InsZsybPvMapper{   
    /**
     * 根据主键查询
     */
    public InsZsBaYbPv getInsPvById(@Param("pkInspv")String pkInspv); 

    /**
     * 查询出所有记录
     */
    public List<InsZsBaYbPv> findAllInsPv();    
    
    /**
     * 保存
     */
    public int saveInsPv(InsZsBaYbPv insPv);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateInsPv(InsZsBaYbPv insPv);
    
    /**
     * 根据主键删除
     */
    public int deleteInsPv(@Param("pkInspv")String pkInspv);
    
    /**
     * 
     * 获取医保入院登记界面的基本信息
     * @param map
     * @return
     */
    public List<Map<String,Object>> getBasicData(Map<String,Object> map);
    
    /**
     * 
     *  CN0016=0的时候获取入院诊断数据   CN0016=2的项目目前没用，不用管
     * @param map
     * @return
     */
    public List<Map<String,Object>> getDiagData(Map<String,Object> map);
    
    /**
     * 
     * CN0016=1的时候获取入院诊断数据   CN0016=2的项目目前没用，不用管
     * @param map
     * @return
     */
    public List<Map<String,Object>> getDiagData2(Map<String,Object> map);
    
    /** CN0016=0的时候获取出院诊断数据   CN0016=2的项目目前没用，不用管
    * @param map
    * @return
    */
   public List<Map<String,Object>> getCyDiagData(Map<String,Object> map);
   
   /** CN0016=1的时候获取出院诊断数据 CN0016=2的项目目前没用，不用管
   * @param map
   * @return
   */
  public List<Map<String,Object>> getCyDiagData2(Map<String,Object> map);
   
   /** 根据住院就诊主键获取医保出院登记界面初始数据
   * @param map
   * @return
   */
  public List<Map<String,Object>> getDischargeRegistrationData(Map<String,Object> map);
  
  /** 根据住院就诊主键获取医保出院登记界面初始数据(异地用的)
  * @param map
  * @return
  */
 public List<Map<String,Object>> getYdDischargeRegistrationData(Map<String,Object> map);
}

