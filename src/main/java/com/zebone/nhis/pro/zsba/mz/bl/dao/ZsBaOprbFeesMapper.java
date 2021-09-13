package com.zebone.nhis.pro.zsba.mz.bl.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 中山人民医院门诊退费结算查询服务
 */
@Mapper
public interface ZsBaOprbFeesMapper {

	/**
	 * 查询结算下的处方
	 * @param mapParam
	 * @return
	 */
	List<Map<String, Object>> querySettlePressRecord(Map<String, String> mapParam);
	
	/**
	 * 查询结算下的检查检验
	 * @param mapParam
	 * @return
	 */
	List<Map<String, Object>> querySettleAssistRecord(Map<String, String> mapParam);
	
	/**
	 * 查询医嘱附加费用信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> qryEtceterasList(Map<String,String> paramMap);
	
	/**
	 * 查询医嘱附加费用明细信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> qryEtceterasItem(Map<String,String> paramMap);
	
	/**
	 * 查询非医嘱费用明细信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> qryCgItemByPkCgop(Map<String,String> paramMap);
	
	/**
	 * 查询已结算的检查检验明细
	 * @param mapParam
	 * @return
	 */
	List<Map<String, Object>> querySettleAssistItem(Map<String, String> mapParam);

	/**
	 * 查询已结算的处方明细
	 * @param pkSettle
	 * @return
	 */
	List<Map<String, Object>> querySettlePressItem(Map<String, String> mapParam);
	List<Map<String, Object>> querySettlePressAddItem(Map<String, String> mapParam);
	
	public Map<String,Object> qryExPresOccStatus(Map<String,Object> paramMap);
}
