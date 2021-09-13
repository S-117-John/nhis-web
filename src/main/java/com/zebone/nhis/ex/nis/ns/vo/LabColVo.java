package com.zebone.nhis.ex.nis.ns.vo;

import java.util.Date;

/**
 * 检验标本执行VO
 * @author wj
 */
public class LabColVo {
    // 就诊主键
    private String pkPv;        
    // 患者主键
    private String pkPi;        
    // 床号
    private String bedNo;        
    // 患者名称
    private String namePi;        
    // 医嘱主键
    private String pkCnord;        
    // 医嘱名称
    private String nameOrd;   
    // 医技申请单
    private String codeApply;
    // 加急标志
     private String flagEmer;        
    // 医嘱号
    private Integer ordsn; 
    // 父医嘱号
    private Integer ordsnParent; 
    // 申请科室
    private String pkDeptApp;        
    // 申请人主键
    private String pkEmpApp;        
    // 申请人名称
    private String nameEmpApp;        
    // 婴儿序号
    private String infantNo;       
    // 医嘱备注
    private String noteOrd;
    // 检验申请单主键
    private String pkOrdlis;        
    // 标本名称
    private String nameSamptype;        
    // 采集科室 - 主键
    private String pkDeptCol;        
    // 采集人 - 主键
    private String pkEmpCol;        
    // 采集人
    private String nameEmpCol;        
    // 采集时间
    private Date dateCol;       
    // 检验申请单状态：0 申请，1 提交，2 采集，3 核收，4 报告
    private String euStatus;        
    // 注意事项
    private String note;        
    // 标本类型
    private String dtSamptype;       
    // 容器类型
    private String dtTubetype;
    // 执行单主键
    private String pkExocc;        
    // 执行机构主键
    private String pkOrgOcc;        
    // 执行机构名称
    private String nameOrgOcc;        
    // 执行科室主键
    private String pkDeptOcc;        
    // 执行科室名称
    private String nameDeptOcc;
    // 计划执行时间
    private Date datePlan;        
    // 记费数量
    private Double quanOcc;
    // 医嘱项目主键
    private String pkOrd;
    // 电子单
    private String formApp;
    //医嘱类型
    private String codeOrdtype;
    //开始时间
    private Date dateStart;
    //医嘱执行护士
    private String pkEmpEx;
    //医嘱执行护士-名称
    private String nameEmpEx;
    //医嘱 执行时间
    private Date datePlanEx;
    //是否记费
    private String FlagBl;
    //检验分组
    private String dtLisgroup;
    
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public String getPkPi() {
		return pkPi;
	}
	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}
	public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public String getNamePi() {
		return namePi;
	}
	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}
	public String getPkCnord() {
		return pkCnord;
	}
	public void setPkCnord(String pkCnord) {
		this.pkCnord = pkCnord;
	}
	public String getNameOrd() {
		return nameOrd;
	}
	public void setNameOrd(String nameOrd) {
		this.nameOrd = nameOrd;
	}
	public String getFlagEmer() {
		return flagEmer;
	}
	public void setFlagEmer(String flagEmer) {
		this.flagEmer = flagEmer;
	}
	public Integer getOrdsn() {
		return ordsn;
	}
	public void setOrdsn(Integer ordsn) {
		this.ordsn = ordsn;
	}
	public String getPkDeptApp() {
		return pkDeptApp;
	}
	public void setPkDeptApp(String pkDeptApp) {
		this.pkDeptApp = pkDeptApp;
	}
	public String getPkEmpApp() {
		return pkEmpApp;
	}
	public void setPkEmpApp(String pkEmpApp) {
		this.pkEmpApp = pkEmpApp;
	}
	public String getNameEmpApp() {
		return nameEmpApp;
	}
	public void setNameEmpApp(String nameEmpApp) {
		this.nameEmpApp = nameEmpApp;
	}
	public String getInfantNo() {
		return infantNo;
	}
	public void setInfantNo(String infantNo) {
		this.infantNo = infantNo;
	}
	public String getNoteOrd() {
		return noteOrd;
	}
	public void setNoteOrd(String noteOrd) {
		this.noteOrd = noteOrd;
	}
	public String getPkOrdlis() {
		return pkOrdlis;
	}
	public void setPkOrdlis(String pkOrdlis) {
		this.pkOrdlis = pkOrdlis;
	}
	public String getNameSamptype() {
		return nameSamptype;
	}
	public void setNameSamptype(String nameSamptype) {
		this.nameSamptype = nameSamptype;
	}
	public String getPkDeptCol() {
		return pkDeptCol;
	}
	public void setPkDeptCol(String pkDeptCol) {
		this.pkDeptCol = pkDeptCol;
	}
	public String getPkEmpCol() {
		return pkEmpCol;
	}
	public void setPkEmpCol(String pkEmpCol) {
		this.pkEmpCol = pkEmpCol;
	}
	public String getNameEmpCol() {
		return nameEmpCol;
	}
	public void setNameEmpCol(String nameEmpCol) {
		this.nameEmpCol = nameEmpCol;
	}
	public Date getDateCol() {
		return dateCol;
	}
	public void setDateCol(Date dateCol) {
		this.dateCol = dateCol;
	}
	public String getEuStatus() {
		return euStatus;
	}
	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getDtSamptype() {
		return dtSamptype;
	}
	public void setDtSamptype(String dtSamptype) {
		this.dtSamptype = dtSamptype;
	}
	public String getDtTubetype() {
		return dtTubetype;
	}
	public void setDtTubetype(String dtTubetype) {
		this.dtTubetype = dtTubetype;
	}
	public String getPkExocc() {
		return pkExocc;
	}
	public void setPkExocc(String pkExocc) {
		this.pkExocc = pkExocc;
	}
	public String getPkOrgOcc() {
		return pkOrgOcc;
	}
	public void setPkOrgOcc(String pkOrgOcc) {
		this.pkOrgOcc = pkOrgOcc;
	}
	public String getNameOrgOcc() {
		return nameOrgOcc;
	}
	public void setNameOrgOcc(String nameOrgOcc) {
		this.nameOrgOcc = nameOrgOcc;
	}
	public String getPkDeptOcc() {
		return pkDeptOcc;
	}
	public void setPkDeptOcc(String pkDeptOcc) {
		this.pkDeptOcc = pkDeptOcc;
	}
	public String getNameDeptOcc() {
		return nameDeptOcc;
	}
	public void setNameDeptOcc(String nameDeptOcc) {
		this.nameDeptOcc = nameDeptOcc;
	}
	public Date getDatePlan() {
		return datePlan;
	}
	public void setDatePlan(Date datePlan) {
		this.datePlan = datePlan;
	}
	public Double getQuanOcc() {
		return quanOcc;
	}
	public void setQuanOcc(Double quanOcc) {
		this.quanOcc = quanOcc;
	}
	public Integer getOrdsnParent() {
		return ordsnParent;
	}
	public void setOrdsnParent(Integer ordsnParent) {
		this.ordsnParent = ordsnParent;
	}
	public String getPkOrd() {
		return pkOrd;
	}
	public void setPkOrd(String pkOrd) {
		this.pkOrd = pkOrd;
	}
	public String getFormApp() {
		return formApp;
	}
	public void setFormApp(String formApp) {
		this.formApp = formApp;
	}
	public String getCodeApply() {
		return codeApply;
	}
	public void setCodeApply(String codeApply) {
		this.codeApply = codeApply;
	}
	public String getCodeOrdtype() {
		return codeOrdtype;
	}
	public void setCodeOrdtype(String codeOrdtype) {
		this.codeOrdtype = codeOrdtype;
	}
	public Date getDateStart() {
		return dateStart;
	}
	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}
	public String getPkEmpEx() {
		return pkEmpEx;
	}
	public void setPkEmpEx(String pkEmpEx) {
		this.pkEmpEx = pkEmpEx;
	}
	public String getNameEmpEx() {
		return nameEmpEx;
	}
	public void setNameEmpEx(String nameEmpEx) {
		this.nameEmpEx = nameEmpEx;
	}
	public Date getDatePlanEx() {
		return datePlanEx;
	}
	public void setDatePlanEx(Date datePlanEx) {
		this.datePlanEx = datePlanEx;
	}
	public String getFlagBl() {
		return FlagBl;
	}
	public void setFlagBl(String flagBl) {
		FlagBl = flagBl;
	}
	public String getDtLisgroup() {
		return dtLisgroup;
	}
	public void setDtLisgroup(String dtLisgroup) {
		this.dtLisgroup = dtLisgroup;
	}
}
