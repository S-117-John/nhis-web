package com.zebone.nhis.cn.ipdw.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.cn.ipdw.vo.CnOpApplyVo;
import com.zebone.nhis.common.module.cn.ipdw.CnOpSubjoin;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface CpRecMapper {
    //查询路径使用记录
	public List<Map<String,Object>> qryCpRec(String pkPv);
	//查询模板维护原因
	public List<Map<String,Object>> qryCpTempReason(@Param("pkCptemp")String pkCptemp,@Param("tempType")String tempType);
	//查询模板应用原因
	public List<Map<String,Object>> qryCpRecReason(String pkCprec);
	//查询阶段医嘱
	public List<Map<String,Object>> qryRecPhaseOrd(@Param("pkCprec")String pkCprec,@Param("pkCpphase")String pkCpphase,@Param("pkOrg")String pkOrg);
	//查询阶段工作
	public List<Map<String,Object>> qryRecPhaseAction(@Param("pkCprec")String pkCprec,@Param("pkCpphase")String pkCpphase);
	//查询路径外医嘱
	public List<Map<String,Object>> qryRecPhaseCnOrd(Map<String,Object> map);
	//查询启用路径的最后一个阶段是否启用
	public Map<String,String> qryTempLastPhase(@Param("pkCprec")String pkCprec,@Param("pkCptemp")String pkCptemp);
	//查询有未停止的长期医嘱，提示停用
	public List<Map<String,Object>> qryRecUnStopOrd(@Param("qryType")String qryType,@Param("pkCpphase")String pkCpphase,@Param("pkCprec")String pkCprec);
	//查询已启用的模板阶段有必选项目
	public int qryTempNecOrd(@Param("pkCpphase")String pkCpphase,@Param("pkCprec")String pkCprec);
	//查询已启用的模板阶段有护士必选项目
	public List<Map<String,Object>> qryTempNsNecOrd(@Param("pkCpphase")String pkCpphase,@Param("pkCprec")String pkCprec);
	//查询有必选项目未做的医嘱，提示是否变异
	public List<Map<String,Object>> qryTempNecCnOrd(@Param("qryType")String qryType,@Param("pkCpphase")String pkCpphase,@Param("pkCprec")String pkCprec);
	//查询单个检查申请
	public Map<String, Object> qryRisOrd(String pkCnord);
	//查询单个检验申请
	public Map<String, Object> qryLisOrd(String pkCnord);
	//查询单个手术申请
	public CnOpApplyVo qryOpOrd(String pkCnord);
	//查询单个手术申请
	public List<CnOpSubjoin> qryOpDtOrd(String pkOrdop);
	//查询路径使用分类医嘱
	public List<Map<String,Object>> qryCateOrdDt(Map<String,Object> map);
	//根据启用路径查询未使用的必选医嘱项目
	public List<Map<String,Object>> qryUnUseNecOrdByRec(String pkCprec);
	//根据启用路径查询未使用的必选工作项目
	public List<Map<String,Object>> qryUnUseNecWorkByRec(String pkCprec);
	//查询启用阶段及之前阶段长期未停止的所有医嘱
	public List<Map<String,Object>> qryRecAllValidPhaseOrd(@Param("pkCprec")String pkCprec);
}
