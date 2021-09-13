package com.zebone.nhis.compay.ins.lb.dao.nhyb;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.compay.ins.lb.nhyb.InsSuzhounhJs;
import com.zebone.nhis.common.module.compay.ins.lb.nhyb.InsSuzhounhReginfo;
import com.zebone.nhis.common.module.compay.ins.lb.nhyb.InsSuzhounhTreat;
import com.zebone.nhis.compay.ins.lb.vo.nhyb.CnOrderAndBdItem;
import com.zebone.platform.modules.mybatis.Mapper;
/**
 * TODO
 * 
 * @author
 */
@Mapper
public interface InsSuZhouNHMapper {

	/**
	 * 模糊查询所有单病种治疗方式记录
	 */
	public List<InsSuzhounhTreat> getInsSuzhounhTreatList(@Param("key")String key);
	
	/**
	 * 按照PK_PV删除宿州农合的登记信息
	 * @param pkpv
	 */
	void deleteRegInfo(String pkpv);
	
	/**
	 * 跟据PKPV查询宿州医保登记信息
	 * @param pkpv
	 * @return
	 */
	InsSuzhounhReginfo getRegInfoByPkPv(String pkpv);
	
	/**
	 * 获取宿州农保结算数据
	 * @param pkSettle
	 * @return
	 */
	InsSuzhounhJs getJsInfoByPkSettle(String pkSettle);
	
	/**
	 * 删除宿州农保结算记录
	 * @param pkSettle
	 */
	void deleteJsInfo(String pkSettle);
	/**
	 * 根据就诊主键获取bl_ip_dt中未上传至医保的收费项目集合和医嘱信息
	 * @param map
	 * @return
	 */
	public List<CnOrderAndBdItem> qryBdItemAndOrderByPkPv(Map<String,Object> map);
	
	/**
	 * 查询农合医保项目与nhis的对照信息
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> qryNhItemWithInfo(Map<String,Object> map);
	
	/**
	 * 查询农合药品和nhis对照项目
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> qryNhPdWithInfo(Map<String,Object> map);

	/**
	 * 宿州农合医保发票打印，需要的结算信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> qrySettleInfoForFpPrint(Map<String,Object> map);

	/**
	 * 查询发票主键
	 * @param paramMap
	 * @return
	 */
	public List<String> qrySettleInfoForFpid(Map<String, Object> paramMap);

	/*
	 * 根据就诊主键获取bl_op_dt中未结算的正的门诊挂号费
	 */
	public List<CnOrderAndBdItem> qryBlOpCgByPkPv(Map<String, Object> map);
}
