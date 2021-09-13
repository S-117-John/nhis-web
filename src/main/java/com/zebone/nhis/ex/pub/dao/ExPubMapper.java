package com.zebone.nhis.ex.pub.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.ex.pub.vo.PatiInfoVo;
import com.zebone.nhis.ex.pub.vo.PdPriceVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface ExPubMapper {
	/**
	 * 查询患者就诊信息
	 * @param paramMap
	 * @return
	 */
	public List<PatiInfoVo> getPatiInfo(Map<String,Object> paramMap);
	/**
	 * 查询预交金
	 * @param paramMap{pkPv}
	 * @return
	 */
	public BigDecimal getYjFee(Map<String,Object> paramMap);
	/**
	 * 查询预交金系数
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> getYjFactor(Map<String,Object> paramMap);
	/**
	 * 查询担保金
	 * @param paramMap{pkPv}
	 * @return
	 */
	public BigDecimal getDbFee(Map<String,Object> paramMap);
	/**
	 * 查询总费用
	 * @param paramMap{pkPv}
	 * @return
	 */
	public BigDecimal getTotalFee(Map<String,Object> paramMap);
	/**
	 * 查询在途费用药品
	 * @param paramMap{pkPv}
	 * @return
	 */
	public BigDecimal getZtPdFee(Map<String,Object> paramMap);
	/**
	 * 查询在途费用非药品
	 * @param paramMap{pkPv}
	 * @return
	 */
	public BigDecimal getZtNPdFee(Map<String,Object> paramMap);
	
	/**
	 * 查询固定费用
	 * @param paramMap{pkPv}
	 * @return
	 */
	public BigDecimal getGdFee(Map<String,Object> paramMap);
	/**
	 * 查询患者自付金额
	 * @param paramMap{pkPv，pkDept}
	 * @return
	 */
	public BigDecimal getZfFee(Map<String,Object> paramMap);
	/**
	 * 获取基数药品价格信息
	 * @param map
	 * @return
	 */
	public List<PdPriceVo> queryPdBasePrice(Map<String,Object> map);
	/**
	 * 根据临床科室查询临床科室与静配中心关系业务线中的静配科室
	 * @param map
	 * @return
	 */
	public Map<String,Object> queryPivasDept(Map<String,Object> map);
	/**
	 * 根据就诊病区 - 统计未执行的相关任务
	 * 2019-03-27 中山二院：添加需求，出院时校验
	 * @param map
	 * @param pk_pv,pk_dept_ns
	 * @returns
	 */
	public List<Map<String, Object>> queryUnExTaskList(Map<String,Object> paramMap);
	/**
	 * 根据科室主键，获取临床-护理单元业务线相关科室
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryCnOrNsDept(Map<String,Object> map);
	/**
	 * 2019-07-22根据字典主键，获取附加属性
	 * @param map{pkDict,codeAtt=0320[是否允许跨院区转科-医保计划]}
	 * @return
	 */
	public List<Map<String,Object>> queryDictAttrByType(Map<String,Object> map);
	
	/**
	 * 获取输液类用法  flag_pivas='1'
	 * @return
	 */
	public List<String> queryBdSupplyPivas();

	/**
	 * 统计产房未执行的相关任务
	 * 2020-10-26 中山博爱：添加需求，出产房时校验
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> queryUnExTaskListForLabor(Map<String, Object> paramMap);

	/**
	 * 统计产房外的职能科室未执行的相关任务
	 * 2020-12-29 中山博爱：添加需求，出产房时校验
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> queryUnExTaskListForFunDept(Map<String, Object> paramMap);

	/**
	 * 统计职能科室为产房未执行的相关任务
	 * 2021-01-20 中山博爱：添加需求，出产房时校验
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> queryUnExTaskListForFunLabor(Map<String, Object> paramMap);

	public List<Map<String,Object>> queryDictAttrByTypes(Map<String,Object> map);
}
