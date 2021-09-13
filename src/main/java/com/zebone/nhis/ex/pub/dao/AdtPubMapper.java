package com.zebone.nhis.ex.pub.dao;

import com.zebone.nhis.ex.nis.pub.vo.CnLabApplyVo;
import com.zebone.nhis.ex.pub.vo.ExlistPubVo;
import com.zebone.nhis.pro.zsba.adt.vo.CnRisApplyBaVo;
import com.zebone.nhis.pro.zsba.adt.vo.LabAndRisTripartiteSystemVo;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdtPubMapper {
	
	/**
	 * 根据就诊主键查询未停止或者未作废的医嘱
	 * @param map
	 * @param pk_pv,sort_no,pk_dept,pk_dept_ns
	 * @return
	 */
	public List<Map<String, Object>> queryOrdByPkPv(Map<String,Object> paramMap);
	
	/**
	 * 根据当前就诊病区，就诊主键查询未停止或者未作废的检查检验
	 * @param map
	 * @param pk_pv，pk_dept_ns,sort_no,pk_dept
	 * @return
	 */
	public List<Map<String, Object>> queryRisByPkPv(Map<String,Object> paramMap);	
	
	/**
	 * 根据就诊主键查询未完成手术
	 * @param map
	 * @param pk_pv
	 * @return
	 */
	public List<Map<String, Object>> queryOpByPkPv(Map<String,Object> paramMap);
	
	/**
	 * 根据就诊主键查询未执行的药品请领单
	 * @param map
	 * @param pk_pv
	 * @returns
	 */
	public List<Map<String, Object>> queryPdapByPkPv(Map<String,Object> paramMap);
	/**
	 * 根据就诊主键查询包床记录
	 * @param map
	 * @param pk_pv
	 * @returns
	 */
	public List<Map<String, Object>> queryPacketBedByPv(Map<String,Object> paramMap);
	/**
	 * 根据就诊主键,当前病区查询未执行医嘱列表
	 * @param map
	 * @param pk_pv,pk_dept_ns,sort_no,pk_dept
	 * @returns
	 */
	public List<ExlistPubVo> queryExlistByPv(Map<String,Object> paramMap);
	
	/**
	 * 根据就诊主键,当前病区查询患者在径处理
	 * @param map
	 * @param pk_pv,pk_dept_ns,sort_no,pk_dept
	 * @returns
	 */
	public List<Map<String, Object>> queryCpByPv(Map<String,Object> paramMap);
	
	/**
	 * 根据就诊主键,当前病区查询患者是否存在同病区的在诊婴儿
	 * @param map
	 * @param pk_pv,pk_dept_ns
	 * @returns
	 */
	public List<Map<String, Object>> queryInfByPv(Map<String,Object> paramMap);
	
	/**
	 * 根据就诊主键、当前日期，查询医保费用核查信息：出院日不可收取费用
	 * 2018-09-06 中山二院：添加需求，出院时校验
	 * @param map
	 * @param pk_pv,pk_dept_ns
	 * @returns
	 */
	public List<Map<String, Object>> queryHpCgChkByPv(Map<String,Object> paramMap);

	/**
	 * 根据就诊主键、当前日期，查询同组多收费用
	 * 2019-05-17 中山二院：添加需求，出院时校验
	 * @param map
	 * @param pk_pv,pk_dept_ns
	 * @returns
	 */
	public List<Map<String, Object>> queryGroupCgChkByPv(Map<String,Object> paramMap);
	
	public List<Map<String, Object>> queryGroupCgChkByPvForSql(Map<String, Object> param);

	public List<Map<String, Object>> queryNsCgChkByPv(Map<String, Object> param);

	public List<Map<String, Object>> queryNsCgChkByPvForOrcl(Map<String, Object> param);

	public List<Map<String, Object>> queryNumByPk(Map<String, Object> param);

	public List<Map<String, Object>> queryRisByPkPvForBoAi(Map<String, Object> param);

	public List<Map<String, Object>> queryOrdReaseByPkPv(Map<String, Object> param);

	/**
	 * 查询计费未做的检验项目
	 */
	List<CnLabApplyVo> getCnlabApplyVoNotDone(Map<String, Object> paramMap);

	/**
	 * 查询计费未做的检查项目
	 */
	List<CnRisApplyBaVo> getCnRisApplyVoNotDone(Map<String, Object> paramMap);

	/**
	 * 查询三方检验信息
	 */
	List<LabAndRisTripartiteSystemVo> getLabTripartiteSystemVos(List<CnLabApplyVo> cnlabApplyVoNotDone);

	/**
	 * 查询三方检查信息
	 */
	List<LabAndRisTripartiteSystemVo> getRisTripartiteSystemVos(List<CnRisApplyBaVo> cnRisApplyVoNotDone);

	/**
	 * 根据记费控制设置查询同组
	 */
    List<Map<String, Object>> queryGroupByPvForSql(Map<String, Object> param);

	/**
	 * 查询大于出院日期费用明细数量
	 * @param
	 * @return
	 */
	Map<String,String>  countBlIpDtAfterDateEnd(@Param("pkPv")String pkPv, @Param("dateEnd")String dateEnd);
}
