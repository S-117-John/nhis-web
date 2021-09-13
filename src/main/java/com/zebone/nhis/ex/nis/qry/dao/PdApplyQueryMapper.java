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
public interface PdApplyQueryMapper {
	
	/**
	 * 查询请领单列表
	 * @param map{pkDeptNs,dateBegin,dateEnd}
	 */
	public List<Map<String,Object>> queryPdApply(Map<String,Object> map);

	/**
	 * 查询统计请领单、退药单
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryPdApplyCount(Map<String,Object> map);
	
	/**
	 * 查询请领单列表
	 * @param map{pkPdap,flagDe,flagPivas,pdname，euAlways}
	 */
	public List<Map<String,Object>> queryPdApDetail(Map<String,Object> map);
	/**
	 * 领药综合查询
	 * @param map{namePi,codePv,dateBegin,dateEnd,flagPivas,pdname，euAlways,euAptype,flagDe,euDirect}
	 */
	public List<Map<String,Object>> queryPdApDeTogether(Map<String,Object> map);
	/**
	 * 查询停发药品明细
	 * @param map{pkDeptAp}
	 * @return
	 */
	public List<Map<String,Object>> queryPdApStopDe(Map<String,Object> map);
}
