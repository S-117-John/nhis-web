package com.zebone.nhis.cn.opdw.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface OpOrderMapper {
	//获得医生医疗权限列表
	public List<Map<String,Object>> getEmpPresAuthority(String pkEmp);	
	//根据临床科室查询取药药房
    public List<Map<String,Object>> getDrugStore(String pkDept);
	//获得患者处方列表
	public List<Map<String,Object>> getPrescription(@Param(value="pkPv")String pkPv,@Param(value="pkEmp")String pkEmp);
	//获得患者处方列表(急诊)
	public List<Map<String,Object>> getPrescriptionNems(@Param(value="pkPv")String pkPv,@Param(value="pkEmp")String pkEmp);
	//获得患者处方明细列表
	public List<Map<String,Object>> getPrescriptionDetail(@Param(value="pkPres")String pkPres,@Param(value="pkDeptExec")String pkDeptExec);
	//获得处方明细所对应的费用
	public List<Map<String,Object>> getPresDetailChargeAdd(String pkPres);
	//获得给药方式费用列表
	public List<Map<String,Object>> getSupplyItem(Map<String,Object> map);
	//获取临床医嘱
	public List<Map> listCnOrder(Map<String, Object> params);
	//查询患者用药信息
	public List<Map<String,Object>> GetPiCurrMonthPdDays(Map<String, Object> params);
}
