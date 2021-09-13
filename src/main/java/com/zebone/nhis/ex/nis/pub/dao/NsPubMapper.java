package com.zebone.nhis.ex.nis.pub.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface NsPubMapper {
	/**
     * 根据pkpv获取患者信息
     * @param map
     * @return
     */
	public Map<String,Object> queryPatiInfoByPV(Map<String,Object> map) ;
	/**
	 * 根据pkPv获取患者转科记录
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryPatiAdtByPV(Map<String,Object> map);
	
	/**
	 * 根据pkPv获取患者床位记录
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryPatiBedByPV(Map<String,Object> map);
	
	/**
	 * 根据pkPv获取患者手术记录
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryPatiOpByPV(Map<String,Object> map);
	
	/**
	 * 根据pkPv获取患者医嘱记录
	 * @param map
	 * @return
	 */
	public List<CnOrder> queryPatiOrdsByPv(@Param("pkPv")String dbName,@Param("list")List<String> codes);
}
