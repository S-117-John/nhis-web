package com.zebone.nhis.ex.nis.pi.dao;

import com.zebone.nhis.ex.nis.pi.vo.NsDeptPatiAmountVo;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface NsDeptPatiAmountMapper {
	/**
	 * 查询病重患者人数
	 * 
	 * @param paramMap
	 * @return
	 */
	public Integer getBzAmount(Map<String,Object> paramMap);

	/**
	 * 查询病危患者人数
	 * 
	 * @param paramMap
	 * @return
	 */
	public Integer getBwAmount(Map<String,Object> paramMap);

	/**
	 * 查询病区患者总人数
	 * 
	 * @param paramMap
	 * @return
	 */
	public Integer getTotal(Map<String,Object> paramMap);

	/**
	 * 查询男性患者人数
	 * 
	 * @param paramMap
	 * @return
	 */
	public Integer getMen(Map<String,Object> paramMap);

	/**
	 * 查询女性患者人数
	 * 
	 * @param paramMap
	 * @return
	 */
	public Integer getWomen(Map<String,Object> paramMap);

	/**
	 * 查询新生儿人数
	 * 
	 * @param paramMap
	 * @return
	 */
	public Integer getNewborn(Map<String,Object> paramMap);

	/**
	 * 其他人数
	 * 
	 * @param paramMap
	 * @return
	 */
	public Integer getOthers(Map<String,Object> paramMap);
	
	/**
	 * 获取病区病重、病危、及总人数
	 * @param paramMap
	 * @return
	 */
	public NsDeptPatiAmountVo getDeptNsPatiNum(Map<String,Object> paramMap);
	/**
	 * 获取病区转入、转出人数
	 * @param paramMap{pkDeptNs,dateBegin,dateEnd,flagIn:1,0}
	 * @return
	 */
	public NsDeptPatiAmountVo getDeptInAndOutNum(Map<String,Object> paramMap);
	/**
	 * 获取病区入院、出院人数
	 * @param paramMap{pkDeptNs,dateBegin,dateEnd,flagIn:1,0}
	 * @return
	 */
	public NsDeptPatiAmountVo getHospInAndOutNum(Map<String,Object> paramMap);
	
	/**
	 * 获取病区出院未结算人数
	 * @param paramMap{pkDeptNs,dateBegin,dateEnd,flagIn:1,0}
	 * @return
	 */
	public NsDeptPatiAmountVo getDeptOutNotSettleNum(Map<String,Object> paramMap);

	/**
	 * 获取一级护理患者明细
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getYJPaint(Map<String, Object> paramMap);

	/**
	 * 获取病重患者明细
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getBZPaint(Map<String, Object> paramMap);

	/**
	 * 获取病危患者明细
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getBWPaint(Map<String, Object> paramMap);

	/**
	 * 获取入院患者明细
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getRYPaint(Map<String, Object> paramMap);


	/**
	 * 获取转入患者明细
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getZRPaint(Map<String, Object> paramMap);


	/**
	 * 获取转出患者明细
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getZCPaint(Map<String, Object> paramMap);

	/**
	 * 获取出院患者明细
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getCYPaint(Map<String, Object> paramMap);

	/**
	 * 获取死亡患者明细
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getSWPaint(Map<String, Object> paramMap);

	/**
	 * 获取手术患者明细
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getSSPaint(Map<String, Object> paramMap);

	/**
	 * 获取出院未结算患者明细
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getJSPaint(Map<String, Object> paramMap);

	/**
	 * 医保患者和自费患者的统计
	 * @param paramMap{pkDeptNs}
	 * @return
	 */
	public NsDeptPatiAmountVo getDeptMedicalInsuranceNum(Map<String,Object> paramMap);
}
