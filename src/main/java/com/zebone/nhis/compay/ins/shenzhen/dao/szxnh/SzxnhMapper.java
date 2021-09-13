package com.zebone.nhis.compay.ins.shenzhen.dao.szxnh;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.compay.ins.shenzhen.vo.szxnh.DiagVo;
import com.zebone.nhis.compay.ins.shenzhen.vo.szxnh.SettlePageDtVo;
import com.zebone.nhis.compay.ins.shenzhen.vo.szxnh.SettlePageHeaderVo;
import com.zebone.nhis.compay.ins.shenzhen.vo.szxnh.XnhBlipdtVo;
import com.zebone.nhis.compay.ins.shenzhen.vo.szxnh.XnhDeptVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface SzxnhMapper {
	/**
	 * 根据pkPv查询新农合住院患者登记流水号
	 * 
	 * @param pkPv
	 * @return
	 */
	public String qryXnhCodePv(String pkPv);

	/**
	 * 根据pkPv查询新农合登记信息和转诊单信息
	 * 
	 * @param pkPv
	 * @return
	 */
	public Map<String, Object> qryXnhPiInfoByPkPv(
			@Param(value = "pkPv") String pkPv,
			@Param(value = "tyPe") String type );

	/**
	 * 根据患者住院登记流水号查询患者结算信息
	 * 
	 * @param pkVisit
	 * @return
	 */
	public Map<String, Object> qryXnhJsInfo(@Param(value = "pkVisit") String pkVisit);

	/** 根据就诊主键pk_pv查询住院收费明细bl_ip_dt的医保上传标志flag_insu为0的记录 */
	public List<XnhBlipdtVo> qryBdItemAndOrderByPkPv(
			@Param(value = "pkPv") String pkPv);

	/** 根据pkPv查询患者出入院状态及关联平台科室 */
	public Map<String, Object> qryPreInpTempInfo(@Param(value = "pkPv") String pkPv);
	
	/**
	 * 查询结算单基本信息
	 * @param pkPv 患者就诊主键
	 * @return
	 */
	public SettlePageHeaderVo qryPageHeaderVo(@Param(value = "pkPv") String pkPv);
	
	/**
	 * 查询结算单费用明细信息
	 * @param pkPv 患者就诊主键
	 * @return
	 */
	public List<SettlePageDtVo> qryPageDtVos(@Param(value = "pkPv") String pkPv);
	
	/**
	 * 获取科室对照信息
	 * @param pkeptadmit
	 * @return
	 */
	public XnhDeptVo qryXNhDeptVo(@Param(value = "pkeptadmit") String pkeptadmit);
	
	/**
	 * 通过患者主键跟就诊主键获取已上传未结算的收费明细表-患者自费总额
	 * @return
	 */
	public String queryShareAmount(Map<String,Object> paramMap);
	
	/**
	 * 
	 * @param pkpv
	 * @return
	 */
	public DiagVo qryDiagVo(@Param(value = "pkPv") String pkpv);
}
