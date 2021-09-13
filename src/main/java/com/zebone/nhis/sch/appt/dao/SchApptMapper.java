package com.zebone.nhis.sch.appt.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.sch.appt.vo.*;
import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.pi.PiLockDt;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface SchApptMapper {

	List<SchApptApplyVo> searchSchApptApply(Map params);

	List<Map<String, Object>> searchSchAppt(Map params);
	/**
	 * 查询现场挂号信息
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> searchPvop(Map params);

	List<CheckApplyVo> checkApply(Map params);

	List<ApplyCheckInfo> getApplyCheckInfo(Map params);
	
	List<ApplyBlacklist> genApplyBlacklist(Map params);
	/**
	 * 获取生成黑名单迟到记录
	 * @param params
	 * @return
	 */
	List<ApplyBlacklist> genApplyBlackLatelist(Map params);

	List<Map<String, Object>> getSchApptBlacklist(Map<String, Object> params);
	/**
	 * 查询迟到记录
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> getSchApptBlackLLatelist(Map<String, Object> params);

	int batchUpdatePiLockDt(List<PiLockDt> piLockDts);

	List<ApplyBlacklist> getPiList(Map<String, String> paramMap);
	
	
	
	List<Map<String, Object>> searchApply_sql();
	
	List<Map<String, Object>> searchApply_oracle();

    List<Map<String, Object>> searchAlreadyApply(Map<String, Object> paramMap);

    List<Map<String, Object>> getCanApplyDeptDetail_sql(@Param("pkDept")String pkDept);

    List<Map<String, Object>> getCanApplyDeptDetail_oracle(@Param("pkDept")String pkDept);

    List<Map<String, Object>> getCanApplyDept_sql(@Param("pkOrg")String pkOrg);

    List<Map<String, Object>> getCanApplyDept_oracle(@Param("pkOrg")String pkOrg);
    /**
     * 获取黑名单审核记录
     * @param paramMap
     * @return
     */
    List<ApplyBlacklist> getPiAuditList(Map<String, Object> paramMap);

    /**
     * 查询科室树
     * @param paramMap
     * @return
     */
	List<Map<String, Object>> getDeptTree(Map<String, Object> paramMap);

	/**
	 * 查询诊区科室树
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> getAreaDeptTree(Map<String, Object> paramMap);

	/**
	 * 按照排班查询 可预约号 等数量统计
	 * @param paramMap
	 * @return
	 */
	List<SchApptCntVo> getApptStatCount(Map<String, Object> paramMap);
	
	/**
     * 获取黑名单的处理记录
     * @param paramMap 
     */
	List<Map<String, Object>> getLockDtlist(@Param("pkPi")String pkPi);
}
