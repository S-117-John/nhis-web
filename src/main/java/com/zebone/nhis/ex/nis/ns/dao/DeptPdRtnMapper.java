package com.zebone.nhis.ex.nis.ns.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.ex.nis.ns.vo.PdRtnDtVo;
import com.zebone.platform.modules.mybatis.Mapper;
/**
 * 药品请领
 * @author yangxue
 *
 */
@Mapper
public interface DeptPdRtnMapper {
	/**
	 * 根据就诊主键查询物品计费记录
	 * @param map{pkPv}
	 * @return
	 */
	public List<PdRtnDtVo> queryCgListByPv(Map<String,Object> map);
	/**
	 * 根据医嘱查询物品执行记录
	 * @param map{pkCnord}
	 * @return
	 */
	public List<Map<String,Object>> queryExListByOrd(Map<String,Object> map);
	
	
}
