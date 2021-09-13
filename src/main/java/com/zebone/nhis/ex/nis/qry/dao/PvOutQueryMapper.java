package com.zebone.nhis.ex.nis.qry.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.ex.nis.ns.vo.OrderCheckVo;
import com.zebone.platform.modules.mybatis.Mapper;
/**
 * 出院患者查询
 * @author yangxue
 *
 */
@Mapper
public interface PvOutQueryMapper {
	
	/**
	 * 根据病区查询出院患者
	 * @param map{dateBegin,dateEnd,pkDeptNs,codePv,namePi,pkDept}
	 */
	public List<Map<String,Object>> queryPvOutList(Map<String,Object> map);

}
