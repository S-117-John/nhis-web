package com.zebone.nhis.pro.sd.scm.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 药监接口
 * @author jd
 *
 */
@Mapper
public interface DrugAdminMapper {
	
	/**
	 * 获取医院药品数据
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getHospDrugList(Map<String,Object> paramMap);
	
	/**
	 * 获取药监药品目录信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> getDrugAdminList(Map<String,Object> paramMap);
	
	/**
	 * 获取药监药品价格信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> getDrugAdminPrice(Map<String,Object> paramMap);
	
	/**
	 * 获取发票汇总信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> getInvSumDataList(Map<String,Object> paramMap);
	
	/**
	 * 获取发票明细信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> getInvDataDtList(Map<String,Object> paramMap);
	
	/**
	 * 获取对照信息
	 * @return
	 */
	public List<Map<String,Object>> getDrugRefMapper(String searchTxt);
	
	/**
	 * 获取三方合同剩余量
	 * @return
	 */
	public List<Map<String,Object>> getDrugAdminContract();
	
	
	/**
	 * 获取配送单下载数据
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> getDrugAdminPrepOrder(Map<String,Object> paramMap);
	
	/**
	 * 获取配送单明细数据
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> getDrugAdminPrepOrderDt(Map<String,Object> paramMap);
	
	/**
	 * 获取上次更新时间
	 * @param paramMap
	 * @return
	 */
	public String getUpdateDate(Map<String,Object> paramMap);
}
