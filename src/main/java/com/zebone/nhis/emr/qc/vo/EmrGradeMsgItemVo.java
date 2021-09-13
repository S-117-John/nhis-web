package com.zebone.nhis.emr.qc.vo;

import java.util.Date;

import com.zebone.nhis.common.module.emr.qc.EmrGradeMsgItem;
import com.zebone.nhis.common.module.emr.rec.rec.EmrPatList;

/**
 *
 * @author 
 */
public class EmrGradeMsgItemVo extends EmrGradeMsgItem{
    
    private String pkPv;
    private String euMsgType;
    private String pkPi;
    private Integer times;
    private String name;
    private String bedNo;
    private String codeIp;
    private String patNo;
    private String ageTxt;
    private Date birthDate;
    private String dtSex;
    private String sexName;
    private String typeName;
    private String standName;
    private String deptName;
    private String empQcName;
    private String empSendName;
    private String flagTime;
    
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public String getEuMsgType() {
		return euMsgType;
	}
	public void setEuMsgType(String euMsgType) {
		this.euMsgType = euMsgType;
	}
	public String getPkPi() {
		return pkPi;
	}
	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}
	public Integer getTimes() {
		return times;
	}
	public void setTimes(Integer times) {
		this.times = times;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public String getCodeIp() {
		return codeIp;
	}
	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}
	public String getPatNo() {
		return patNo;
	}
	public void setPatNo(String patNo) {
		this.patNo = patNo;
	}
	public String getAgeTxt() {
		return ageTxt;
	}
	public void setAgeTxt(String ageTxt) {
		this.ageTxt = ageTxt;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public String getDtSex() {
		return dtSex;
	}
	public void setDtSex(String dtSex) {
		this.dtSex = dtSex;
	}
	public String getSexName() {
		return sexName;
	}
	public void setSexName(String sexName) {
		this.sexName = sexName;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getStandName() {
		return standName;
	}
	public void setStandName(String standName) {
		this.standName = standName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getEmpQcName() {
		return empQcName;
	}
	public void setEmpQcName(String empQcName) {
		this.empQcName = empQcName;
	}
	public String getEmpSendName() {
		return empSendName;
	}
	public void setEmpSendName(String empSendName) {
		this.empSendName = empSendName;
	}
	public String getFlagTime() {
		return flagTime;
	}
	public void setFlagTime(String flagTime) {
		this.flagTime = flagTime;
	}
    
}