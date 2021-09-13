package com.zebone.nhis.ex.nis.qry.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.ex.nis.ns.vo.OrderCheckVo;
import com.zebone.platform.modules.mybatis.Mapper;
/**
 * 医技预约查询
 * @author yangxue
 *
 */
@Mapper
public interface MedSchAppQueryMapper {
	
	/**
	 * 根据病区查询医技预约信息
	 * @param map{pkDeptNs,pkDept,pkDeptEx,dateEnd,dateBegin,codePv,bedNo,namePi}
	 */
	public List<Map<String,Object>> queryMedSchApp(Map<String,Object> map);

}
