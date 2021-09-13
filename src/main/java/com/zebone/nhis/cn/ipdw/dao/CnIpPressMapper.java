package com.zebone.nhis.cn.ipdw.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.cn.ipdw.CnTransApply;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface CnIpPressMapper {
	
	/**
	 * 处方页获取一个处方下的所有医嘱
	 * @param pkPv
	 * @return
	 */
	public List<Map<String,Object>> qryCnOrder(String pkPv);
	
	/**
	 * 获取一个患者的处方
	 * @param pkPv
	 * @return
	 */
	public List<Map<String,Object>> qryCnPress(String pkPv);
	
	/**
	 * 获取当前科室对应的库房
	 */
	public List<Map<String,Object>> qryDrugStore(String pkPv);
	
	/**
	 * 获取当前医嘱的库存量 map{pk_store,pk_ord}
	 * @param m
	 * @return
	 */
	public Integer qryStoreQuanMin(Map m);
	
	/**
	 * 获取当前医嘱的执行科室
	 * @param m
	 * @return
	 */
	public List<Map<String,Object>> qryDeptExec(Map m);
	
	
	/**
	 * 获取当前业务线下的执行科室
	 * @param m
	 * @return
	 */
	public List<Map<String,Object>> qryBussinessLine(String pkDept);
	
	/**
	 * 获取输血打印单
	 * @param params
	 * @return
	 */
	public List<CnTransApply> getApplies(Map<String,Object> params);
	
	public List<BdOuDept> queryEmpAndDept(Map<String, String> params);
	
}
