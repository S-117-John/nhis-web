package com.zebone.nhis.cn.ipdw.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.cn.ipdw.vo.BdTermDiagDeptVo;
import com.zebone.nhis.cn.ipdw.vo.BdTermDiagTreatwayVo;
import com.zebone.nhis.common.module.base.bd.code.BdTermDiagTreatway;
import com.zebone.nhis.common.module.base.bd.mk.BdCndiag;
import com.zebone.nhis.common.module.cn.ipdw.CnDiag;
import com.zebone.nhis.common.module.cn.ipdw.PvDiagDt;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface CnDiagMapper {

	  	//查询当前诊断
		public List<Map<String,Object>> qryPvDiag(String pkPv);
		//查询当前诊断
		public List<Map<String,Object>> qryPvDiagBA(String pkPv);
	   //查询医生常用诊断
	   public List<BdTermDiagDeptVo> qryDoctorCommDiag(Map<String,Object> paramMap);
	   //查询诊断的备注信息
	   public List<Map<String,Object>> qryCnDiagComt(Map<String,Object> paramMap);
	   //查询诊断的备注信息明细
	   public List<Map<String,Object>> qryCnDiagComtDt(Map<String,Object> paramMap);
	   //删除多余的明细
	   public void deldiagdtByList(Map<String, Object> map);
	   //查询分值
	   public List<BdTermDiagTreatwayVo> qryVal(Map map);
	   //查询诊断明细
	   public List<PvDiagDt> qryPvdiagDt(String pkPvdiag);
	   //查询就诊类型
	   public String qryEuPvtype(String pkPv);
	   //查询当前机构常用诊断
	   public List<BdTermDiagDeptVo> qryTermDiagDept(String pkOrg);
	   //查询所有的诊断
	   public List<PvDiagDt> qryPvDiagDtNew(List list);
	   //查询标准诊断
	   public BdCndiag qryBdCndiag(String pkDiag);
	   //查询常用诊断
	   public List<BdTermDiagDeptVo> qryTermDiagDepts();
	   //查询多余数据
	   public List<String> qryPkDiags(String pkPv);
	   //删除多余的pv_diag_dt
	   public void delpvDiagDtByPkpvdiag(List<String> list);
	   //删除多余的pv_diag
	   public void delPvdiagByList(Map map);
	   //查询主键是否存在
	   public Integer countPvdiag(String pkPvdiag);
	   //查询历史诊断
	   public List<CnDiag> qryCnDiag(String pkPv);
	   //查询主键是否重复
	   public Integer countTermDiagDept(String pkDiagdept);
	   //是否有传染病
	   public List<Map<String, Object>> countTermDiag(List<String> list);
	   //查询患者出院时间
	   public Map<String, Object> qryPiInfo(String pkPv);
	   //查询机构专业类型
	   public Map<String, Object> qryDeptMedicaltype(String pkDept);
	   //根据诊断名称查询icd
	   public List<Map<String,Object>> qryBdCndiagByNameCd(Map map);
	   
	   public List<Map<String, Object>> qrySpecDiag(String labresult);
	   
	   //查询医生常用诊断Drg
	   public List<Map<String, Object>> qryDoctorCommDiagDrg(Map<String,Object> paramMap);
	   /**
	    * 查询医生当前诊断DRG
	    * @param pkPv
	    * @return
	    */
	   public List<Map<String,Object>> qryPvDiagDrg(String pkPv);
}
