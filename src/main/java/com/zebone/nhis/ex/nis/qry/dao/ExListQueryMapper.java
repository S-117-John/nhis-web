package com.zebone.nhis.ex.nis.qry.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.ex.nis.ns.vo.OrderCheckVo;
import com.zebone.platform.modules.mybatis.Mapper;
/**
 * 医嘱执行单查询
 * @author yangxue
 *
 */
@Mapper
public interface ExListQueryMapper {
	
	/**
	 * 根据医嘱查询执行单信息
	 * @param map{pkOrd}
	 */
	public List<Map<String,Object>> queryExlistByCon(Map<String,Object> map);
	
	/**
	 * 根据计费明细查询执行列表
	 * @param map{pkCgip}
	 * @return
	 */
	public List<Map<String,Object>> queryExlistByCg(Map<String,Object> map);
	
	
	
}
