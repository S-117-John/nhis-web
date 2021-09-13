package com.zebone.nhis.compay.ins.lb.service.pub.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.compay.ins.lb.vo.szyb.InsSzybItemMap;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface LbYbPubMapper {
	/**
	 * 根据住院收费主键跟新bl_ip_dt中的flag_ins
	 * @param pkCgips
	 */
	public void updateFlagInsuByPk(List<String> pkCgips);
	
	/**
	 * 根据住院收费主键跟新bl_ip_dt中的flag_ins
	 * @param pkCgips
	 */
	public void updateOpFlagInsuByPk(List<String> pkCgops);
	
	/**
	 * 更新医保结算表的结算主键,传入表名TableName, 主键名称PrimaryKeyName ,主键的值PrimaryKeyValue,需要更新的字段 PkSettle
	 * @param paramMap
	 */
	public void updatePkSettlByYBPk(Map<String,Object> paramMap);
	
	/**
	 * 查询宿州医保对照信息
	 * @param param
	 * @return
	 */
	public List<InsSzybItemMap> qrySzybItemMapInfo(Map<String,Object> param);
	/**
	 * 查询宿州各医保药品与医院药品未匹配信息
	 * @param paramMap {"euMatch":"匹配状态（1：匹配，2：未匹配）", "pkHp":"医保主键", "xmlb":"项目类别", "info":"文本框搜索信息（暂未使用）"}
	 * @return
	 */
	public List<Map<String,Object>> qryYbPdDicNoWithInfo(Map<String,Object> paramMap);
	
	/**
	 * 查询宿州各医保收费项目与医院信息未匹配信息
	 * @param paramMap {"euMatch":"匹配状态（1：匹配，2：未匹配）", "pkHp":"医保主键", "xmlb":"项目类别", "info":"文本框搜索信息（暂未使用）"}
	 * @return
	 */
	public List<Map<String,Object>> qryYbItemDicNoWithInfo(Map<String,Object> paramMap);
	
	/**
	 * 根据收费编码查询宿州医保各项自付总额
	 * @param paramMap{"pkPv":"就诊主键","codeBills":"List<收费编码>"}
	 * @return {["codeBill":"","amountPi":"自费"]}
	 */
	public List<Map<String,Object>> qrySzybFymx(Map<String,Object> paramMap);
	
	/**
	 * 根据收费编码查询宿州离休医保各项自付总额
	 * @param paramMap{"pkPv":"就诊主键","codeBills":"List<收费编码>"}
	 * @return {["codeBill":"","amountPi":"自费"]}
	 */
	public List<Map<String,Object>> qrySzlxFymx(Map<String,Object> paramMap);
	
	/**
	 * 根据收费编码查询宿州农合医保各项自付总额
	 * @param paramMap{"pkPv":"就诊主键","codeBills":"List<收费编码>"}
	 * @return {["codeBill":"","amountPi":"自费"]}
	 */
	//public List<Map<String,Object>> qrySznhFymx(Map<String,Object> paramMap);
	
	/**
	 * 根据项目类别和发票分类查询账单码
	 * @param paramMap {"pkItem":"收费项目分类","pkInvcate":"院内票据分类主键"}
	 * @return {"codeBill":"账单码"}
	 */
	public String qryCodeBillByPkItem(Map<String,Object> paramMap);

	/**
	 * 根据就诊主键查询门诊收费明细
	 * @param paramMap{"pkPv":"就诊主键"}
	 * @return
	 */
	public List<Map<String,Object>> qryOpFymx(Map<String,Object> paramMap);

	/**
	 * 根据就诊主键查询住院收费明细
	 * @param paramMap {"pkPv":"就诊主键"}
	 * @return
	 */
	public List<Map<String,Object>> qryIpFymx(Map<String,Object> paramMap);
	
	/**
	 * 根据id查询结算表的数据
	 * @param paramMap{"id":"结算表主键","tableName":"表名"}
	 * @return
	 */
	public Map<String,Object> qryYbJsByTableId(Map<String,Object> paramMap);
	
	/**
	 * 根据id查询结算表的数据
	 * @param paramMap{"id":"结算表主键","tableName":"表名"}
	 * @return
	 */
	public Map<String,Object> qryBdHpFactorByPkDeptAndBdHp(Map<String,Object> paramMap);
		
}
