package com.zebone.nhis.cn.ipdw.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.cn.ipdw.vo.BdFlowSpVo;
import com.zebone.nhis.cn.ipdw.vo.SyxCpTempDeptVo;
import com.zebone.nhis.cn.ipdw.vo.SyxCpTempFormPhaseVo;
import com.zebone.nhis.common.module.cn.cp.SyxCpTemp;
import com.zebone.nhis.common.module.cn.cp.SyxCpTempCpord;
import com.zebone.nhis.common.module.cn.cp.SyxCpTempDiag;
import com.zebone.nhis.common.module.cn.cp.SyxCpTempPhase;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface SyxCpTempMapper {

	//查询临床路径模板
	List<Map<String,Object>> qryCpTemp(Map<String,Object> map);
	//是否启用审批流
	List<BdFlowSpVo> qryBdFlow();	
	//查询路径模板基本信息
	SyxCpTemp qryCpTempBase(Map<String,Object> map);
	//查询路径审批信息
	List<BdFlowSpVo> qryCpTempFlow(Map<String,Object> map);
	//查询模板适用诊断
	List<SyxCpTempDiag> qryCpTempDiag(Map<String,Object> map);	
	//查询模板适用科室
	List<SyxCpTempDeptVo> qryCpTempDept(Map<String,Object> map);
	//查询模板阶段
	List<SyxCpTempPhase> qryCpTempPhase(Map<String,Object> map);
	//查询模板表单医嘱
	List<SyxCpTempFormPhaseVo> qryCpTempForm(Map<String,Object> map);	
	//查询路径阶段的工作和医嘱
	List<SyxCpTempCpord> qryCpTempPhaseOrd(Map<String,Object> map);
	
}
