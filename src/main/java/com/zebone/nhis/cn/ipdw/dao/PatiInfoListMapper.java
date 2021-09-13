package com.zebone.nhis.cn.ipdw.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PatiInfoListMapper {
	
	/**
	 * 病人信息列表的医嘱列表
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> qryCnOrder(Map<String,Object> param);
	/**
	 * 病人信息列表的医嘱列表和转存资料
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> qryCnOrderAndDump(Map<String,Object> param);
	
	/**
	 * 病人信息列表的诊断列表
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> qryCnDiag(String pkpv);
	
	/**
	 * 病人信息列表的体征列表
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> qryCnOcc(String pkpv);
	
	/**
	 * 病人信息列表的检查列表
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> qryCnRisOcc(String pkpv);
	
	/**
	 * 病人信息列表的检验列表
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> qryCnLabOcc(String pkpv);

	public List<Map<String,Object>> qryInfor(String pkDept);
	


}
