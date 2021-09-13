package com.zebone.nhis.labor.nis.dao;

import com.zebone.nhis.labor.nis.vo.PvLaborVo;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PvLaborAdtMapper {
	/**
	 * 根据不同条件查询孕产就诊记录,查询条件可扩展
	 * @param paramMap{pkOrg,....}
	 * @return
	 */
	public List<PvLaborVo> queryPvLabor(Map<String,Object> paramMap);
	
	/**
	 * 查询产科待接收的患者列表
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> queryPatisByIn(Map<String,Object> paramMap);
	/**
	 * 根据条件查询允许入产房患者
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> queryPatisByQuery(Map<String, Object> paramMap);

	/**
	 * 查询患者是否出院
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> quertOutPvInfo(Map<String, Object> paramMap);
}
