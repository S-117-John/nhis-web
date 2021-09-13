package com.zebone.nhis.scm.opds.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface ScmOpPresCheck2Mapper {
	
	/**
	 * 查询未签到的患者
	 * @param map{"pkDeptEx":"执行科室"}
	 * @return{"codePi": "患者编码", "pkPi":"患者主键","namePi":"患者姓名" }
	 */
	public List<Map<String,Object>> qryPrePatis(Map<String,Object> map);
	
	/**
	 * 查询患者处方信息
	 * @param map{"pkDeptEx":"执行机构","pkPi":"患者主键"} 
	 * @return 患者处方信息
	 */
	public List<Map<String,Object>> qryPres(Map<String,Object> map);
	
	/**
	 * 查询患者处方详情
	 * @param map {"pkPresocces":"处方主键集合"}
	 * @return 处方详情
	 */
	public List<Map<String,Object>> qryPresDt(List<String> paramMap); 
	
	/**
	 * 配药发药模式下更新处方执行单
	 * @param map{"dateReg":,"winnoConf":"发药窗口","winnoPrep":"配药窗口","pkPresocc":"处方主键"}
	 */
	public void updateByDosage(Map<String,Object> map);
	
	
	/**
	 * 直接发药模式下更新处方执行单
	 * @param map{"dateReg":,"winnoConf":"发药窗口","pkPresocc":"处方主键"}
	 */
	public void updateByDispensing(Map<String,Object> map);
}
