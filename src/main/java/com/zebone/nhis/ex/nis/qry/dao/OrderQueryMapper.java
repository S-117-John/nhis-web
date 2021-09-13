package com.zebone.nhis.ex.nis.qry.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface OrderQueryMapper {
	
	/**
	 * 根据患者查询医嘱信息
	 * @param map{pkPv,nameOrd,euAlways,euStatus}
	 */
	public List<Map<String,Object>> queryOrdByCon(Map<String,Object> map);
	/**
	 * 根据计费明细查询医嘱
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryOrdByCg(Map<String,Object> map);
	
	/**
	 * 根据科室查询医嘱信息
	 * @param map{pkPv,nameOrd,euAlways,euStatus}
	 */
	public List<Map<String,Object>> queryOrdByDept(Map<String,Object> map);
	
	/**
	 * 根据科室查询医嘱信息
	 * @param map{pkPv,nameOrd,euAlways,euStatus}
	 */
	public List<Map<String,Object>> queryPiInfoByPv(Map<String,Object> map);
}
