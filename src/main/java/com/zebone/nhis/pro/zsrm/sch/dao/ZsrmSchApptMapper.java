package com.zebone.nhis.pro.zsrm.sch.dao;

import com.zebone.nhis.pro.zsrm.sch.vo.SchApptCntVo;
import com.zebone.nhis.pro.zsrm.sch.vo.SchSchVo;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface ZsrmSchApptMapper {

	List<String> getSchOfApptGroup(Map<String,Object> params);
	
	List<SchSchVo> getSchOfApptInfo(Map<String,Object> params);
	
	/**
	 * 按照排班查询 可预约号 等数量统计
	 * @param paramMap
	 * @return
	 */
	List<SchApptCntVo> getApptStatCount(Map<String, Object> paramMap);

	/**
	 * 查询资源主信息（子节点）
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> getSimpleSchResource(Map<String, Object> paramMap);

	/**
	 * 查询资源主信息（主节点）
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> getSimpleSchResourceFather(Map<String, Object> paramMap);
	
    /**
     * 查询科室树
     * @param paramMap
     * @return
     */
	List<Map<String, Object>> getDeptTree(Map<String, Object> paramMap);

	/**
	 * 查询预约数据（表格）
	 * @param params
	 * @return
	 */
	public List<SchSchVo> getSchInfoData(Map<String, Object> params);

	/**
	 * 查询号表预约信息
	 * @param pkSchSet
	 * @return
	 */
	public List<Map<String, Object>> getTicketInfo(@Param("pkSchSet") Set<String> pkSchSet);
}
