package com.zebone.nhis.bl.pub.syx.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.base.bd.price.BdInvcate;
import com.zebone.nhis.common.module.bl.BlCc;
import com.zebone.nhis.common.module.bl.BlCcPay;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 门诊日结账、汇总结账-中二
 * 
 * @author yangxue
 *
 */
@Mapper
public interface OpBlccRegMapper {
	/**
	 * 查询结账记录
	 * @param paramMap
	 * @return
	 */
	public  List<Map<String,Object>> queryBlccList(Map<String,Object> paramMap);
	
	/**
	 * 查询结账明细对应的支付信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>>  queryBlccByPaymode(Map<String,Object> paramMap);
	
	/**
	 * 查询结账发票号段信息
	 * @param paramMap
	 * @return
	 */
	public BlCc queryBlCcInfoByBlccPay(Map<String,Object> paramMap);
	/**
	 * 根据支付方式查询发票补打张数
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> queryReceiptByPaymode(Map<String,Object> paramMap);
	
	/**
	 * 查询未结账明细对应的支付信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>>  queryUnBlccByPaymode(Map<String,Object> paramMap);
	/**
	 * 统计未结账发票号段及张数
	 * @param paramMap
	 * @return
	 */
	public Map<String,Object> queryUnBlccReceipt(Map<String,Object> paramMap);
	/**
	 * 统计未结账退号收据
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> queryUnBlccRtnReceipt(Map<String,Object> paramMap);
	
	/**查询未结账支付明细信息*/
	public List<BlCcPay> qryUnBlccPayDetail(Map<String,Object> paramMap);
	/**
	 * 查询是否可取消结账数据
	 * @param pkCc
	 * @return
	 */
	public List<Map<String,Object>> queryCanCancelData(@Param(value="pkCc")String pkCc);
	
	/**
	 * 查询发票分类
	 * @param paramMap
	 * @return
	 */
	public BdInvcate queryInvcateByCate(Map<String,Object> paramMap);
	
}
