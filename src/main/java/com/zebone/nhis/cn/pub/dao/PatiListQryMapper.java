package com.zebone.nhis.cn.pub.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.pi.pub.vo.PibaseVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PatiListQryMapper {
	//根据患者分类 查询科室患者列表
	public List<Map<String,Object>> queryDeptPatiList(Map<String,Object> map) ;
	List<Map<String,Object>> queryDeptNsPatiList(Map<String,Object> map) ;
	//查询在院患者床位信息列表
	public List<Map<String,Object>> queryPatiBedList(Map<String,Object> map) ;

	//查询在院患者床位信息列表---护理单元
	public List<Map<String,Object>> queryPatiBedNsList(Map<String,Object> map) ;

	//根据患者分类查询出院患者(包括本科室出院，本科室转出出院)
	public List<Map<String,Object>> qryLeavePatiList(Map<String,Object> map);
	//根据患者分类 查询转科患者列表
	public List<Map<String,Object>> queryTransPatiList(Map<String,Object> map) ;
	//根据患者分类 查询会诊患者列表
	public List<Map<String,Object>> queryConsultPatiList(Map<String,Object> map) ;
	//查询科室在科总人数
	public int qryDeptPatiTotal(String pk_dept);
	//查询科室今日收手人数
	public int qryDeptOpToday(Map<String, Object> map);
	//查询科室明日手术人数
	public int qryDeptOpTomorrow(Map<String, Object> map);
	//查询科室入院人数
	public int qryDeptPatiInNum(Map<String,Object> map);
	//查询科室出院人数
	public int qryDeptPatiOutNum(Map<String,Object> map);
	//查询科室转入人数
	public int qryDeptPatiTransInNum(Map<String,Object> map);
	//查询科室转出人数
	public int queryDeptPatiTransOutNum(Map<String,Object> map);
	//查询科室病重人数
	public int queryDeptPatiDiseaseNum(Map<String,Object> map);
	//查询科室病危人数
	public int queryDeptPatiDangerNum(Map<String,Object> map);
	//查询科室临床路径在径人数
	public int queryDeptCpRecNum(Map<String,Object> map);
	//查询业务下科室对应仓库
	public List<Map<String,Object>> qryStoreByDept(Map<String,Object> map);
	//根据床号 查询产房患者
    public List<Map<String,Object>> queryLaborPati(Map<String,Object> map) ;
	//查询患者未结算费用
    public List<Map<String,Object>> qryPiAmount(Map<String,Object> map) ;
	//查询患者病种限额 
    public List<Map<String,Object>> qryPiTreatWayAmount(Map<String,Object> map) ;
    //查询单个病人信息
  	public Map<String,Object> querySinglePat(Map<String,Object> map) ;  	
  	//查询单个病人信息---修改病人信息时使用
  	Map<String,Object> queryPati(Map<String,Object> map) ;
  	//查询患者医保信息
	List<Map> searchPvDeti(Map map);
	/** 根据患者住院号或者名字查询诊断信息vo列表 */
	List<Map> getPibaseVoByPi(Map map);
}
