package com.zebone.nhis.ma.pub.platform.send.impl.pskq.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PskqResourcePoolSendMapper {
	
	/**
	 * 查询挂号信息
	 * @param pkPv
	 * @return
	 */
    public List<Map<String, Object>> getPatiOpRegInfo(String pkPv);
	
	/**
	 * 查询挂号费用信息
	 * @param pkPv
	 * @return
	 */
    public List<Map<String, Object>> getPatiOpRegChargeInfo(String pkPv);
    
	/**
	 * 查询入院登记信息
	 * @param pkPv
	 * @return
	 */
    public List<Map<String, Object>> getPatiAdtRegInfo(String pkPv);
    
	/**
	 * 查询出院结算信息
	 * @param pkPv
	 * @return
	 */
    public List<Map<String, Object>> getPatiHospInfo(String pkPv);
    
}
