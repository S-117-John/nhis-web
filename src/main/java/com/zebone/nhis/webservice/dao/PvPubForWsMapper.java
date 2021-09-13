package com.zebone.nhis.webservice.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PvPubForWsMapper {
	/**
	 * 根据患者标识查询患者详细信息
	 * @param map pkPi:患者唯一标识   codePi:患者编码  idno:身份证号  codeIp:住院号  codeCard:就诊卡号
	 * @return
	 */
	public List<Map<String,Object>> queryPiMaster(Map<String,Object> map);

	/**
	 * 根据患者标识查询患者详细信息
	 * @param map pkPi:患者唯一标识   codePi:患者编码  idno:身份证号  codeIp:住院号  codeCard:就诊卡号
	 * @return
	 */
	public List<PiMaster> queryLbzyPiMaster(Map<String,Object> map);
	/**
	 * 查询患者分类信息
	 * @return
	 */
	public List<Map<String,Object>> getPiCateInfo();
	/**
	 * 查询是否存在就诊卡
	 * @param piCard
	 * @return
	 */
	public List<Map<String,Object>> getPiCard(Map<String,Object> paramMap);
	
	/**
	 * 查询患者当前就诊状态的住院就诊信息
	 * @param param pkPi:患者唯一标识  pkDept:就诊科室[可选] 就诊状态  codeIp:住院号pkDept:就诊科室 dateBegin:开始日期 dateEnd:截止日期
	 * @return
	 */
	public List<Map<String, Object>> getPvInfoByIp (Map<String, Object> param);
	/**
	 * 查询当前患者就诊状态的门诊就诊信息
	 * @param parampkPi:患者唯一标识 codeOp:门诊号[可选] pkDept:就诊科室[可选]
	 * @return
	 */
	public List<Map<String, Object>> getPvInfoByOp (Map<String, Object> param);
	/**
	 * 查询当前患者就诊结束状态的门诊就诊信息
	 * @param parampkPi:患者唯一标识 codeOp:门诊号[可选] pkDept:就诊科室[可选]
	 * @return
	 */
	public List<Map<String, Object>> getPvInfoByOpStatus (Map<String, Object> param);
	
	/** 根据患者主键获取最大的就诊次数(门诊) */
	public Integer LbgetMaxOpTimes(String pkPi);
	
	/**
	 * 床旁系统查询患者当前就诊状态的住院就诊信息  
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> getPvInfoByPv(Map<String,Object> param);
	/**
	 * 查询就诊医保计划 
	 * @param param
	 * @return
	 */
	public Map<String,Object> LbgetPvHp(Map<String,Object> param);
	
	/**
	 * 查询住院患者就诊信息
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> queryPiListInHosp(Map<String,Object> param);
	/**
	 * 查询患者信息输血
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> queryPiMasterByTmis(Map<String,Object> param);
	
	/**
	 * 查询科室血型分布（输血
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> queryMasterBlood(List<String> depts);
}
