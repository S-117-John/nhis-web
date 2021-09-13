package com.zebone.nhis.ma.pub.platform.send.impl.syx.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface SyxPlatFormSendAdtMapper {
	public List<Map<String, Object>> qryPiInfo(String pkPi);
	
	public Map<String, Object> qryOrg(String pkOrg);
	
	public Map<String, Object> qryPvEn(String pkPv);
	
	public Map<String, Object> qryEmployee(String pkEmpPhy);
	
	public Map<String, Object> qryDept(String pkDept);
	
	public Map<String, Object> qryIp(String pkPv);
	
	public List<Map<String, Object>> qryDiag(String pkPv);
	
	public Map<String, Object> qryPvAll(String pkPv);
	
	/**
	 * 查询转科信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> qryPvAdtInfo(Map<String,Object> paramMap);
	
	public String qryPkPv(String pkPi);
	
	public List<Map<String,Object>> qryIcd(List<String> list);
	
	public List<Map<String,Object>> getMainHpInfo(String pkPi);
	
   /*
	* 查询出院信息
    */
	public List<Map<String, Object>> qryPvOutAll(String pkPv);

	public String qryCodeEmpByEmployee(String pkEmpDiag);

	

	
}
