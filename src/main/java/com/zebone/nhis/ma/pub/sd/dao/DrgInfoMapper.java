package com.zebone.nhis.ma.pub.sd.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface DrgInfoMapper {
	/**
	 * 查询医院信息(drg上传医院信息用)
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> queryOrgList(Map<String, Object> paramMap);
	
	/**
	 * 科室匹配接口
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> queryDeptList(Map<String, Object> paramMap);
	/**
	 * 医生信息接口
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> queryDoctorList(Map<String, Object> paramMap);
	
	/**
	 * 查询冲销数据
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> queryMinDataOff(Map<String, Object> paramMap);
	/**
	 * 查询冲销数据
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> queryServiceDetail(Map<String, Object> paramMap);
	
	/**
	 * 查询病案质控
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> queryControl(Map<String, Object> paramMap);
	
	/*****************以下为最小数据集接口用************************/
	
	/**
	 * 基本信息
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> queryBasicInfo(Map<String, Object> paramMap);
	/**
	 * 转科信息
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> queryTransferInfo(Map<String, Object> paramMap);
	/**
	 * 重症监护
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> queryICUInfo(Map<String, Object> paramMap);
	
	/**
	 * 查询诊断信息
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> queryDiagInfo(Map<String, Object> paramMap);
	/**
	 * 查询诊断信息
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> queryCCHIInfo(Map<String, Object> paramMap);
	/**
	 * 查询费用信息
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> queryItemInfo(Map<String, Object> paramMap);
	
}
