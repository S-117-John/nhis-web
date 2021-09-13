package com.zebone.nhis.ex.pub.vo;

import java.util.Date;

import com.zebone.nhis.common.module.ex.nis.ns.ExOrderOcc;

/**
 * 若此对象用来计算医嘱执行数量时，ExOrderOcc类中的quanOcc，pkCnord字段为必传属性
 * @author IBM
 *
 */
public class GenerateExLisOrdVo extends ExOrderOcc{
	/*就诊类型*/
	private String euPvtype;
	/*医嘱类型编码*/
	private String codeOrdtype;
	/*重复类型*/
	private String euAlways;
	/*频次编码*/
	private String codeFreq;
	/*医嘱开始时间*/
	private Date dateStart;
	/*最近一次执行时间*/
	private Date dateLastEx;
	/*处方主键*/
	private String pkPres;
	/*付数*/
	private double ords;
	/*变频医嘱标志*/
	private String flagPlan;
	/*频次周期类型（0按天执行 1按周执行  2按小时执行 ）*/
	private String euCycle;
	/*首日量标志*/
	private String flagFirst;//废弃，使用firstNum
	/**
	 * 首日数量，不为null的情况均认为设置了首日数量
	 */
	private Integer firstNum;//
	/*药品标志*/
	private String flagDurg;
	/*停止时间*/
	private Date dateStop;
	
	
	//以下为扩展字段，不用于计算医嘱对应的执行数量
	private String nameOrd;//医嘱名称
	private double days;//天数
	private double dosage;//剂量
	private String pkUnitDos;//剂量单位
	private String codeSupply;
	private String pkSupplycate;
	private String bedNo;//床号
	private String namePi;//姓名
	private String ordNote;//备注
	private String namefreq;//频次
	private String namesupply;//用法
	private String noteSupply;
	private String pkInsu;//医保计划
	private String pkOrgExec;
	private String pkDeptExec;
	private String ordsn;
	private String ordsnParent;
	private String pkDept;//开立科室
	private String nameEmpOrd;//开立医生
	
	
	public Integer getFirstNum() {
		return firstNum;
	}
	public void setFirstNum(Integer firstNum) {
		this.firstNum = firstNum;
	}
	public String getPkSupplycate() {
		return pkSupplycate;
	}
	public void setPkSupplycate(String pkSupplycate) {
		this.pkSupplycate = pkSupplycate;
	}
	public String getNameEmpOrd() {
		return nameEmpOrd;
	}
	public void setNameEmpOrd(String nameEmpOrd) {
		this.nameEmpOrd = nameEmpOrd;
	}
	public String getEuCycle() {
		return euCycle;
	}
	public void setEuCycle(String euCycle) {
		this.euCycle = euCycle;
	}
	public String getFlagPlan() {
		return flagPlan;
	}
	public void setFlagPlan(String flagPlan) {
		this.flagPlan = flagPlan;
	}
	public String getPkDept() {
		return pkDept;
	}
	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
	public String getEuPvtype() {
		return euPvtype;
	}
	public void setEuPvtype(String euPvtype) {
		this.euPvtype = euPvtype;
	}
	public String getCodeOrdtype() {
		return codeOrdtype;
	}
	public void setCodeOrdtype(String codeOrdtype) {
		this.codeOrdtype = codeOrdtype;
	}
	public String getEuAlways() {
		return euAlways;
	}
	public void setEuAlways(String euAlways) {
		this.euAlways = euAlways;
	}
	public double getDosage() {
		return dosage;
	}
	public void setDosage(double dosage) {
		this.dosage = dosage;
	}
	public String getPkUnitDos() {
		return pkUnitDos;
	}
	public void setPkUnitDos(String pkUnitDos) {
		this.pkUnitDos = pkUnitDos;
	}
	public String getCodeFreq() {
		return codeFreq;
	}
	public void setCodeFreq(String codeFreq) {
		this.codeFreq = codeFreq;
	}
	public Date getDateStart() {
		return dateStart;
	}
	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}
	public Date getDateLastEx() {
		return dateLastEx;
	}
	public void setDateLastEx(Date dateLastEx) {
		this.dateLastEx = dateLastEx;
	}
	public String getCodeSupply() {
		return codeSupply;
	}
	public void setCodeSupply(String codeSupply) {
		this.codeSupply = codeSupply;
	}
	
	public String getNameOrd() {
		return nameOrd;
	}
	public void setNameOrd(String nameOrd) {
		this.nameOrd = nameOrd;
	}
	public double getDays() {
		return days;
	}
	public void setDays(double days) {
		this.days = days;
	}
	public String getPkInsu() {
		return pkInsu;
	}
	public void setPkInsu(String pkInsu) {
		this.pkInsu = pkInsu;
	}
	public String getPkPres() {
		return pkPres;
	}
	public void setPkPres(String pkPres) {
		this.pkPres = pkPres;
	}
	public String getNoteSupply() {
		return noteSupply;
	}
	public void setNoteSupply(String noteSupply) {
		this.noteSupply = noteSupply;
	}
	public double getOrds() {
		return ords;
	}
	public void setOrds(double ords) {
		this.ords = ords;
	}
	public Date getDateStop() {
		return dateStop;
	}
	public void setDateStop(Date dateStop) {
		this.dateStop = dateStop;
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
	public String getOrdNote() {
		return ordNote;
	}
	public void setOrdNote(String ordNote) {
		this.ordNote = ordNote;
	}
	public String getNamefreq() {
		return namefreq;
	}
	public void setNamefreq(String namefreq) {
		this.namefreq = namefreq;
	}
	public String getNamesupply() {
		return namesupply;
	}
	public void setNamesupply(String namesupply) {
		this.namesupply = namesupply;
	}
	public String getFlagFirst() {
		return flagFirst;
	}
	public void setFlagFirst(String flagFirst) {
		this.flagFirst = flagFirst;
	}
	public String getFlagDurg() {
		return flagDurg;
	}
	public void setFlagDurg(String flagDurg) {
		this.flagDurg = flagDurg;
	}
	public String getPkOrgExec() {
		return pkOrgExec;
	}
	public void setPkOrgExec(String pkOrgExec) {
		this.pkOrgExec = pkOrgExec;
	}
	public String getPkDeptExec() {
		return pkDeptExec;
	}
	public void setPkDeptExec(String pkDeptExec) {
		this.pkDeptExec = pkDeptExec;
	}
	public String getOrdsn() {
		return ordsn;
	}
	public void setOrdsn(String ordsn) {
		this.ordsn = ordsn;
	}
	public String getOrdsnParent() {
		return ordsnParent;
	}
	public void setOrdsnParent(String ordsnParent) {
		this.ordsnParent = ordsnParent;
	}
	
	
}
