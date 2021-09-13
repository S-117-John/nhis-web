package com.zebone.nhis.cn.ipdw.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.platform.modules.mybatis.DynaBean;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface QueryChargeMapper {
	/**
	 * 获取患者的预交金，担保金，费用，自费费用
	 * @param pkPv
	 * @return
	 */
	List<DynaBean> getChargeSummary(String pkPv);
	/**
	 * 获取患者的预交金 
	 * @param pkPv
	 * @return
	 */
	double getChargeSumDeposit(String pkPv);
	/**
	 * 获取患者的担保金 
	 * @param pkPv
	 * @return
	 */
	double getChargeSumGuarantee(String pkPv);
	/**
	 * 获取患者的费用 
	 * @param pkPv
	 * @return
	 */
	double getChargeSumAmount(String pkPv);
	/**
	 * 获取患者的自费费用
	 * @param pkPv
	 * @return
	 */
	double getChargeSumAmountPi(String pkPv);
	/**
	 * 获取患者分类汇总费用信息
	 * @param pkPv
	 * @return
	 */
	List<DynaBean> getChargeByCategory(Map<String,Object>map);
	/**
	 * 获取患者项目汇总费用信息
	 * @param pkPv
	 * @return
	 */
	List<DynaBean> getChargeByItem(Map<String,Object>map);
	/**
	 * 获取患者项目汇总费用信息和转存数据
	 * @param pkPv
	 * @return
	 */
	List<DynaBean> getChargeByItemAndDump(Map<String,Object>map);
	/**
	 * 获取患者费用明细
	 * @param pkPv
	 * @return
	 */
	List<DynaBean> getChargeDetail(Map<String,Object> map);
	
	/**
	 * 获取患者费用明细和转存数据
	 * @param pkPv
	 * @return
	 */
	List<DynaBean> getChargeDetailAndDump(Map<String,Object> map);

	List<DynaBean> queryEncHistory(Map<String,String> map);
}
