package com.zebone.nhis.pro.zsba.adt.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.pro.zsba.adt.vo.NHISPatientWxMapper;
import com.zebone.nhis.pro.zsba.adt.vo.TPatientWxMapper;
import com.zebone.nhis.pro.zsba.adt.vo.ZsbaHisAdtPv;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 入院登记-中山mapper
 * 
 * @author wangjie
 * @date 2019年12月04日
 *
 */
@Mapper
public interface ZsAdtRegMapper {
	
	/**
	 * 查询【旧his】系统中的历史就诊记录
	 * @param map
	 * @return
	 */
	public List<ZsbaHisAdtPv> getHisPvList(Map<String,Object> map);
	
	/**
	 * 查询【旧病案】系统中的历史就诊记录
	 * @param map
	 * @return
	 */
	public List<ZsbaHisAdtPv> getBaglPvList(Map<String,Object> map);

	/**
	 * 查询【Nhis】系统中的历史就诊记录
	 * @param map
	 * @return
	 */
	public List<ZsbaHisAdtPv> getNhisPvList(Map<String,Object> map);

	/**
	 * 查询【his】系统中的门诊待缴费清单
	 * @param map{patientId-患者编码,}
	 * @return
	 */
	public List<Map<String,Object>> getOpFeeListFromHis(Map<String,Object> map);

	/**
	 * 查询【nhis】系统中的门诊待缴费清单
	 * @param map{patientId-患者编码,}
	 * @return
	 */
	public List<Map<String,Object>> getOpFeeListFromNhis(Map<String,Object> map);
	
	/**
	 * 查询【旧his】系统中的科室列表
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getHisDeptList(Map<String,Object> map);

	/**
	 * 查询【Nhis】系统中的已对照科室列表
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getNhisDeptMapList(Map<String,Object> map);

	/**
	 * 查询【his】系统中的微信列表
	 * @param map
	 * @return
	 */
	public List<TPatientWxMapper> getPatientWxList(Map<String,Object> map);

	/**
	 * 查询【nhis】系统中的微信列表
	 * @param map
	 * @return
	 */
	public List<NHISPatientWxMapper> getNPatientWxList(Map<String,Object> map);

	/**
	 * 查询【Nhis】系统中的患者列表
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryPatiListByWg(Map<String,Object> map);

	/**
	 * 根据医嘱查询计费明细
	 * @param map{pkOrd}
	 * @return
	 */
	public List<Map<String,Object>> queryBlCgIpDetailsByOrd(Map<String,Object> map) ;

	/**
	 * 根据医嘱查询试管费用
	 * @param map{pkOrd}
	 * @return
	 */
	public List<Map<String,Object>> querySampFeeByOrd(Map<String,Object> map) ;

	/**
	 * 查询患者费用核查信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> queryPatiDetails(Map<String, Object> map);
	
	/**
	 * 查询患者费用核查明细信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> queryCgDetaileds(Map<String, Object> map);
	
	/**
	 * 查询待同步医技申请单状态的就诊记录
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> queryNeedSysStatusPatis(Map<String, Object> map);

	/**
	 * 查询旧his系统，门诊患者信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getPiListFromHis(Map<String, Object> paramMap);

	/**
	 * 查询nhis系统，患者主索引信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getPiListFromNhis(Map<String, Object> paramMap);
	
}
