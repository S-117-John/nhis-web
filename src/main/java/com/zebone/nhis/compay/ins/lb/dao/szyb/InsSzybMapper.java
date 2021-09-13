package com.zebone.nhis.compay.ins.lb.dao.szyb;


import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.compay.ins.lb.szyb.InsSzybFymx;
import com.zebone.nhis.common.module.compay.ins.lb.szyb.InsSzybJs;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.compay.ins.lb.vo.szyb.ParamLoginInSave;
import com.zebone.nhis.compay.ins.lb.vo.szyb.StReceVo;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface InsSzybMapper {
	/**
	 * 查询宿州市医保结算信息
	 * @param param{ywzqh：业务周期号}
	 * @return
	 */
	public List<InsSzybJs> querySzybJsList(Map<String,Object> param);
	/**
	 * 查询宿州市医保结算汇总信息
	 * @param param{ywzqh：业务周期号}
	 * @return
	 */
	public Map<String,Object> querySzybJsSummary(Map<String,Object> param);
	
	/**
	 * 根据ywlsh：业务流水号查询费用明细
	 * @param paramMap
	 * @return
	 */
	public List<InsSzybFymx> qryFymxInfo(Map<String,Object> paramMap);
	
	/**
	 * 根据签到人查询签到信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> qryLoginreCord(Map<String,Object> paramMap);
	
	/**
	 * 根据业务周期号查询结算中各个费用总和
	 * @param paramMap
	 * @return
	 */
	public Map<String,Object> qrySumSzybJsKind(Map<String,Object> paramMap);
	
	public void updateLoginrecord(ParamLoginInSave loginInSave);

	/**根据科室主键查询标准部门编码信息*/
	List<Map<String,Object>> qryDtSttypeByPkdept(@Param("pkList")List<String> pkList);

	/**查询收费项目对照信息*/
	List<Map<String,Object>> qryItemMapInfo(Map<String,Object> paramMap);

	/**查询人员备案信息*/
	List<Map<String,Object>> qryInsuEmpInfo();

	/**查询医保结算信息*/
	List<StReceVo> qryStInfo(Map<String,Object> paramMap);

	/**查询医保支付明细*/
	List<Map<String,Object>> qryInsuPayDtls(Map<String,Object> paramMap);

	/**查询住院诊断信息*/
	List<Map<String,Object>> qryIpDiagList(Map<String,Object> paramMap);

	/**查询费用明细信息*/
	List<Map<String,Object>> qryChargeDtls(Map<String,Object> paramMap);

	/**查询手术信息*/
	List<Map<String,Object>> qryIclDtls(Map<String,Object> paramMap);

	/**查询医保结算信息*/
	List<Map<String,Object>> qryInsuStInfo(Map<String,Object> paramMap);

	/**根据pkSettle查询结算信息*/
	List<Map<String,Object>> qryStInfoByPkSettle(Map<String,Object> paramMap);

	/**根据pkSettle查询就诊信息*/
	PvEncounter qryPvInfoByPkSettle(Map<String,Object> paramMap);

	/**查询门诊医保结算信息*/
	List<StReceVo> qryOpStInfo(Map<String,Object> paramMap);

	/**查询门诊医保支付明细*/
	List<Map<String,Object>> qryOpInsuPayDtls(Map<String,Object> paramMap);

	/**查询门诊诊断信息*/
	List<Map<String,Object>> qryOpDiagList(Map<String,Object> paramMap);

	/**查询门诊费用明细信息*/
	List<Map<String,Object>> qryOpChargeDtls(Map<String,Object> paramMap);

}
