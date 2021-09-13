package com.zebone.nhis.scm.ipdedrug.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.mybatis.Mapper;
/**
 * 发药单打印-查询接口
 * @author yangxue
 *
 */
@Mapper
public interface IpDeDrugPrintMapper {

	/**
	 * 根据发药单查询发药汇总
	 * @param paramMap{codeDe,pkOrg}
	 * @return
	 * @throws BusException
	 */
	public List<Map<String, Object>> qryIpDeDrugSummary(Map<String,Object> paramMap) throws BusException;

	/**
	 * 根据发药单查询发药明细
	 * @param paramMap{codeDe,pkOrg}
	 * @return
	 * @throws BusException
	 */
	public List<Map<String, Object>> qryIpDeDrugDetail(Map<String,Object> paramMap) throws BusException;
	/**
	 * 根据发药单号查询处方类型列表
	 * @return
	 * @throws BusException
	 */
	public List<Map<String,Object>> queryDePresTypeList(Map<String,Object> paramMap)throws BusException;

	/**
	 * 根据请领单主键更新打印状态
	 * @param paramMap
	 */
	public void updatePrintApply(List<String> pkPdaps);
	
	/**
	 * 打印配药单校验是否取消
	 * @param pkPdaps
	 * @return
	 */
	public int checkPrintApply(List<String> pkPdaps);
	
	/**
	 * 住院处方发药：汇总
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> qryPrintPresSum(Map<String,Object> paramMap);
	
	/**
	 * 住院处方发药查询发药记录
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> qryPresListData(Map<String,Object> paramMap);
	
	
	/**
	 * 住院处方打印标签
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> qryPrintLableData(Map<String,Object> paramMap);
	
	/**
	 * 住院草药处方打印
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>>  qryPrintHrebPresInfo(Map<String,Object> paramMap);
}
