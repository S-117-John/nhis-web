package com.zebone.nhis.pv.adt.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 入院登记mapper
 * 
 * @author wangpeng
 * @date 2016年9月22日
 *
 */
@Mapper
public interface AdtRegMapper {
	
	/** 根据患者主键获取最大的就诊次数(住院) */
	Integer getMaxIpTimes(String pkPi);
	
	/** 根据就诊主键查询就诊记录 */
	PvEncounter getPvEncounterByPkPv(String pkPv);
	
	/** 根据患者主键查询患者信息 */
	PiMaster getPiMasterByPkPi(String pkPi);

	public BigDecimal getYjFee(String pkPi);
	public List<Map<String,Object>> getYjFactor(String pkPi);
	public BigDecimal getTotalFee(String pkPi);
	public BigDecimal getZtPdFee(List<Map<String, Object>> pvList);	
	public BigDecimal getZtNPdFee(String pkPi);
}
