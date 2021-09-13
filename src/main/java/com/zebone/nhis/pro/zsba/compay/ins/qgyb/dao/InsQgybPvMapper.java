package com.zebone.nhis.pro.zsba.compay.ins.qgyb.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 全国医保
 * @author Administrator
 *
 */
@Mapper
public interface InsQgybPvMapper {
	
    /** 根据住院就诊主键和时间段获取住院收费明细 - 项目  数量为正
     * @param map
     * @return
     */
    public List<Map<String,Object>> getChargeDetails(Map<String,Object> map);
    
    /** 根据住院就诊主键和时间段获取住院收费明细 - 药品 数量为正
     * @param map
     * @return
     */
    public List<Map<String,Object>> getChargeDetailsToYp(Map<String,Object> map);
    
    /** 根据住院就诊主键和时间段获取住院收费明细 - 项目 数量为负
     * @param map
     * @return
     */
    public List<Map<String,Object>> getChargeDetailsRefund(Map<String,Object> map);
    
    /** 根据住院就诊主键和时间段获取住院收费明细 - 药品数量为负
     * @param map
     * @return
     */
    public List<Map<String,Object>> getChargeDetailsToYpRefund(Map<String,Object> map);
    
    /** 获取日间诊断
     * @param map
     * @return
     */
    public List<Map<String,Object>> getRjzd(Map<String,Object> map);
    
    /** 获取医保结算清单上传的Setlinfo的数据
     * @param map
     * @return
     */
    public Map<String,Object> get4101Setlinfo(Map<String,Object> map);
    
    /** 获取医保结算清单上传的payinfo的数据
     * @param map
     * @return
     */
    public List<Map<String,Object>> get4101Payinfo(Map<String,Object> map);
    
    /** 获取医保结算清单上传的Diseinfo的数据
     * @param map
     * @return
     */
    public List<Map<String,Object>> get4101Diseinfo(Map<String,Object> map);
    
    /** 获取医保结算清单上传的iteminfo的数据,数据来源2301接口 
     * @param map
     * @return
     */
    public List<Map<String,Object>> get4101Iteminfo2301(Map<String,Object> map);
    
    /** 获取医保结算清单上传的iteminfo的数据 ，数据来源5204接口
     * @param map
     * @return
     */
    public List<Map<String,Object>> get4101Iteminfo5204(Map<String,Object> map);
    
    /** 获取医保结算清单上传的oprninfo的数据 
     * @param map
     * @return
     */
    public List<Map<String,Object>> get4101Oprninfo(Map<String,Object> map);
    
    /** 获取医保结算清单上传的西医诊断信息，用于打印 
     * @param map
     * @return
     */
    public List<Map<String,Object>> get4101DiseinfoXy(Map<String,Object> map);
    
    /** 获取医保结算清单上传的中医诊断信息，用于打印 
     * @param map
     * @return
     */
    public List<Map<String,Object>> get4101DiseinfoZy(Map<String,Object> map);
    
    /** 获取医保结算清单上传的Setlinfo的数据，用于打印
     * @param map
     * @return
     */
    public Map<String,Object> get4101SetlinfoPrint(Map<String,Object> map);
    
    /** 查询病案首页信息
     * @param map
     * @return
     */
    public Map<String,Object> getEmrHomePage(Map<String,Object> map);
    
    /** 获取医保结算清单上传的Setlinfo的数据(旧系统)
     * @param map
     * @return
     */
    public Map<String,Object> get4101SetlinfoJxt(Map<String,Object> map);
    
    /** 获取医保结算清单上传的Diseinfo的数据(旧系统)
     * @param map
     * @return
     */
    public List<Map<String,Object>> get4101DiseinfoJxt(Map<String,Object> map);
    
    /** 获取医保结算清单上传的oprninfo的数据 (旧系统)
     * @param map
     * @return
     */
    public List<Map<String,Object>> get4101OprninfoJxt(Map<String,Object> map);
    
    /** 获取医保结算清单上传的Diseinfo的数据(从病案系统取值)
     * @param map
     * @return
     */
    public List<Map<String,Object>> get4101DiseinfoBaxt(Map<String,Object> map);
    
    /** 获取医保结算清单上传的oprninfo的数据 (从病案系统取值)
     * @param map
     * @return
     */
    public List<Map<String,Object>> get4101OprninfoBaxt(Map<String,Object> map);
}
