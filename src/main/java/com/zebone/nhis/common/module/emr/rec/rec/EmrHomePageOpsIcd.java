package com.zebone.nhis.common.module.emr.rec.rec;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

@Table(value="emr_home_page_ops_icd")
public class EmrHomePageOpsIcd extends BaseModule{

	@PK
	@Field(value="pk_ops_icd",id=KeyId.UUID)
	private String pkOpsIcd;
	
	@Field(value="pk_page")
	private String pkPage;
	
	@Field(value="pk_pv")
	private String pkPv;//就诊主键
	
	@Field(value="seq_no")
	private String seqNo;//序号
	
	@Field(value="seq_no_sub")
	private String seqNoSub;//子序号

	@Field(value="eu_type")
	private String euType;//类型
	
	@Field(value="op_code")
	private String opCode;//手术编码
	
	@Field(value="op_name")
	private String opName;//手术名称
	
	@Field(value="op_date")
	private Date opDate;//手术日期
	
	@Field(value="grade_code")
	private String gradeCode;//手术级别编码
	
	@Field(value="grade_name")
	private String gradeName;//手术级别名称
	
	@Field(value="pk_emp_op")
	private String pkEmpOp;//手术医师
	
	@Field(value="op_doc_name")
	private String opDocName;//手术医师名称
	
	@Field(value="pk_emp_opi")
	private String pkEmpOpi;//手术I助
	
	@Field(value="opi_name")
	private String opiName;//手术I助名称
	
	@Field(value="pk_emp_opii")
	private String pkEmpOpii;//手术II助
	
	@Field(value="opii_name")
	private String opiiName;//手术II助名称
	
	@Field(value="incision_type_code")
	private String incisionTypeCode;//手术切口类别编码
	
	@Field(value="incision_type_name")
	private String incisionTypeName;//手术切口类别名称
	
	@Field(value="heal_grade_code")
	private String healGradeCode;//手术切口愈合等级代码
	
	@Field(value="heal_grade_name")
	private String healGradeName;//手术切口愈合等级名称
	
	@Field(value="anes_type_code")
	private String anesTypeCode;//麻醉方式编码
	
	@Field(value="anes_type_name")
	private String anesTypeName;//麻醉方式名称
	
	@Field(value="pk_emp_anes")
	private String pkEmpAnes;//麻醉医师
	
	@Field(value="anes_doc_name")
	private String anesDocName;//麻醉医师名称
	
	@Field(value="asa_code")
	private String asaCode;//麻醉分级编码
	
	@Field(value="asa_name")
	private String asaName;//麻醉分级名称
	
	@Field(value="nnis_code")
	private String nnisCode;//手术风险分级编码
	
	@Field(value="nnis_name")
	private String nnisName;//手术风险分级名称
	
	@Field(value="op_code_icd")
	private String opCodeIcd;//手术编码病案
	
	@Field(value="op_name_icd")
	private String opNameIcd;//手术名称-病案

	@Field(value="flag_elective")
	private String flagElective;//择期手术
	
	@Field(value="pk_parent")
	private String pkParent;//父手术主键
	
	@Field(value="flag_extra")
	private String flagExtra;//附加手术标志
	
	@Field(value="remark")
	private String remark;
	
	@Field(value="op_code_si")
	private String opCodeSi;
	
	@Field(value="op_name_si")
	private String opNameSi;
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPkParent() {
		return pkParent;
	}

	public void setPkParent(String pkParent) {
		this.pkParent = pkParent;
	}

	public String getFlagExtra() {
		return flagExtra;
	}

	public void setFlagExtra(String flagExtra) {
		this.flagExtra = flagExtra;
	}

	public String getFlagElective() {
		return flagElective;
	}

	public void setFlagElective(String flagElective) {
		this.flagElective = flagElective;
	}

	public String getPkOpsIcd() {
		return pkOpsIcd;
	}

	public void setPkOpsIcd(String pkOpsIcd) {
		this.pkOpsIcd = pkOpsIcd;
	}

	public String getPkPage() {
		return pkPage;
	}

	public void setPkPage(String pkPage) {
		this.pkPage = pkPage;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}

	public String getEuType() {
		return euType;
	}

	public void setEuType(String euType) {
		this.euType = euType;
	}

	public String getOpCode() {
		return opCode;
	}

	public void setOpCode(String opCode) {
		this.opCode = opCode;
	}

	public String getOpName() {
		return opName;
	}

	public void setOpName(String opName) {
		this.opName = opName;
	}

	public Date getOpDate() {
		return opDate;
	}

	public void setOpDate(Date opDate) {
		this.opDate = opDate;
	}

	public String getGradeCode() {
		return gradeCode;
	}

	public void setGradeCode(String gradeCode) {
		this.gradeCode = gradeCode;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getPkEmpOp() {
		return pkEmpOp;
	}

	public void setPkEmpOp(String pkEmpOp) {
		this.pkEmpOp = pkEmpOp;
	}

	public String getOpDocName() {
		return opDocName;
	}

	public void setOpDocName(String opDocName) {
		this.opDocName = opDocName;
	}

	public String getPkEmpOpi() {
		return pkEmpOpi;
	}

	public void setPkEmpOpi(String pkEmpOpi) {
		this.pkEmpOpi = pkEmpOpi;
	}

	public String getOpiName() {
		return opiName;
	}

	public void setOpiName(String opiName) {
		this.opiName = opiName;
	}

	public String getPkEmpOpii() {
		return pkEmpOpii;
	}

	public void setPkEmpOpii(String pkEmpOpii) {
		this.pkEmpOpii = pkEmpOpii;
	}


	public String getOpiiName() {
		return opiiName;
	}

	public void setOpiiName(String opiiName) {
		this.opiiName = opiiName;
	}

	public String getIncisionTypeCode() {
		return incisionTypeCode;
	}

	public void setIncisionTypeCode(String incisionTypeCode) {
		this.incisionTypeCode = incisionTypeCode;
	}

	public String getIncisionTypeName() {
		return incisionTypeName;
	}

	public void setIncisionTypeName(String incisionTypeName) {
		this.incisionTypeName = incisionTypeName;
	}

	public String getHealGradeCode() {
		return healGradeCode;
	}

	public void setHealGradeCode(String healGradeCode) {
		this.healGradeCode = healGradeCode;
	}

	public String getHealGradeName() {
		return healGradeName;
	}

	public void setHealGradeName(String healGradeName) {
		this.healGradeName = healGradeName;
	}

	public String getAnesTypeCode() {
		return anesTypeCode;
	}

	public void setAnesTypeCode(String anesTypeCode) {
		this.anesTypeCode = anesTypeCode;
	}

	public String getAnesTypeName() {
		return anesTypeName;
	}

	public void setAnesTypeName(String anesTypeName) {
		this.anesTypeName = anesTypeName;
	}

	public String getPkEmpAnes() {
		return pkEmpAnes;
	}

	public void setPkEmpAnes(String pkEmpAnes) {
		this.pkEmpAnes = pkEmpAnes;
	}

	public String getAnesDocName() {
		return anesDocName;
	}

	public void setAnesDocName(String anesDocName) {
		this.anesDocName = anesDocName;
	}

	public String getAsaCode() {
		return asaCode;
	}

	public void setAsaCode(String asaCode) {
		this.asaCode = asaCode;
	}

	public String getAsaName() {
		return asaName;
	}

	public void setAsaName(String asaName) {
		this.asaName = asaName;
	}

	public String getNnisCode() {
		return nnisCode;
	}

	public void setNnisCode(String nnisCode) {
		this.nnisCode = nnisCode;
	}

	public String getNnisName() {
		return nnisName;
	}

	public void setNnisName(String nnisName) {
		this.nnisName = nnisName;
	}

	public String getOpCodeIcd() {
		return opCodeIcd;
	}

	public void setOpCodeIcd(String opCodeIcd) {
		this.opCodeIcd = opCodeIcd;
	}

	public String getOpNameIcd() {
		return opNameIcd;
	}

	public void setOpNameIcd(String opNameIcd) {
		this.opNameIcd = opNameIcd;
	}

	public String getSeqNoSub() {
		return seqNoSub;
	}

	public void setSeqNoSub(String seqNoSub) {
		this.seqNoSub = seqNoSub;
	}

	public String getOpCodeSi() {
		return opCodeSi;
	}

	public void setOpCodeSi(String opCodeSi) {
		this.opCodeSi = opCodeSi;
	}

	public String getOpNameSi() {
		return opNameSi;
	}

	public void setOpNameSi(String opNameSi) {
		this.opNameSi = opNameSi;
	}
	
}
