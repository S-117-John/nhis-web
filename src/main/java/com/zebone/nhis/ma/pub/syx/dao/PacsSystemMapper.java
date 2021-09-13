package com.zebone.nhis.ma.pub.syx.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.ma.pub.syx.vo.Tfunctionitemlistforpacs;
import com.zebone.nhis.ma.pub.syx.vo.Tfunctionrequestforpacs;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PacsSystemMapper {

	/**
	 * 根据申请单主键查询检查系统接口表需要的字段（子表）
	 * @param pkExoccList
	 * @return
	 */
	public List<Tfunctionrequestforpacs> qryRequestForPacs(Map<String,Object> pkExoccList);
	
	/**
	 * 根据申请单主键查询检查系统接口表需要的字段（子表）
	 * @param pkExoccList
	 * @return
	 */
	public List<Tfunctionrequestforpacs> qryOpRequestForPacs(Map<String,Object> pkExoccList);

	/**
	 * 根据申请单主键查询检查系统接口表需要的字段（主表）
	 * @param pkExoccList
	 * @return
	 */
	public List<Tfunctionitemlistforpacs> qryItemListForPacs(Map<String,Object> pkExoccList);
	
	/**
	 * 根据申请单主键查询检查系统接口表需要的字段（主表）
	 * @param pkExoccList
	 * @return
	 */
	public List<Tfunctionitemlistforpacs> qryOpItemListForPacs(Map<String,Object> pkExoccList);
	
	/**
	 * 查询已存在中间库的申请单号
	 * @param tfunctionitemlistforpacs
	 * @return
	 */
	public List<String> qryRepeatApply(List<Tfunctionrequestforpacs> tfunctionrequestforpacsList);
	
	public List<String> qryItemRepeatApply(List<Tfunctionrequestforpacs> tfunctionrequestforpacsList);
	
	/**
	 * 更新检查申请单
	 * @param codeApplys
	 */
	public void updateRisApply(List<String> codeApplys);
	
	/**
	 * 查询医嘱主键
	 * @param codeApplys
	 * @return
	 */
	public List<String> qryPkCnord(List<String> codeApplys);	
	
	public List<Map<String,Object>> qryLisApply(@Param("codePi")String codePi,@Param("codeIp")String codeIp);
	
	public List<Map<String,Object>> qryRisApply(@Param("codePi")String codePi,@Param("codeIp")String codeIp);
	
	public List<Map<String,Object>> qryPaApply(@Param("codePi")String codePi,@Param("codeIp")String codeIp);
	
	public List<Map<String,Object>> qryNmApply(@Param("codePi")String codePi,@Param("codeIp")String codeIp);
	
	public List<Map<String,Object>> qryLisResult(@Param("lisLableNo")String lisLableNo);
	
	//查询科室患者列表
	public List<Map<String, Object>> qryPatientList(Map<String, Object> map);
	
	//检查报告-心电图
	public List<Map<String, Object>> qryXnlApply(@Param("codePi")String codePi,@Param("codeIp")String codeIp);
	//检查报告-超声
	public List<Map<String, Object>> qryXusApply(@Param("codePi")String codePi,@Param("codeIp")String codeIp);
	//查询出院患者列表
	public List<Map<String, Object>> qryLeavePatientList(Map<String, Object> map);
	//查询转科患者列表
	public List<Map<String, Object>> qryChangePatientList(Map<String, Object> map);
	//查询会诊患者列表
	public List<Map<String, Object>> qryConsultationPatientList(Map<String, Object> map);

}
