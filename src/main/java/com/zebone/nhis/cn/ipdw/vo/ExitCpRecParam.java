package com.zebone.nhis.cn.ipdw.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.cn.cp.CpRecReason;

public class ExitCpRecParam {
  //退出
  public String pkCprec;
  public List<CpRecReason> cpRecReason = new ArrayList<CpRecReason>();
  public String pkCpexp;
  public String expNote;
  //启用
  public String pkPv;
  public String pkCptemp;
  public String pkDiag;
  public String euReason;
  public String euRecType;
  //转路径
  public String isTurnToCp;
  //变异类型
  public String expType;
  //阶段变异
  public String phasePkCpexp;
  public String phaseExpNote;
  //阶段
  public String pkCpphase;
public String getPkCprec() {
	return pkCprec;
}
public void setPkCprec(String pkCprec) {
	this.pkCprec = pkCprec;
}
public List<CpRecReason> getCpRecReason() {
	return cpRecReason;
}
public void setCpRecReason(List<CpRecReason> cpRecReason) {
	this.cpRecReason = cpRecReason;
}
public String getPkCpexp() {
	return pkCpexp;
}
public void setPkCpexp(String pkCpexp) {
	this.pkCpexp = pkCpexp;
}
public String getPkPv() {
	return pkPv;
}
public void setPkPv(String pkPv) {
	this.pkPv = pkPv;
}
public String getPkCptemp() {
	return pkCptemp;
}
public void setPkCptemp(String pkCptemp) {
	this.pkCptemp = pkCptemp;
}
public String getPkDiag() {
	return pkDiag;
}
public void setPkDiag(String pkDiag) {
	this.pkDiag = pkDiag;
}
public String getEuReason() {
	return euReason;
}
public void setEuReason(String euReason) {
	this.euReason = euReason;
}
public String getEuRecType() {
	return euRecType;
}
public void setEuRecType(String euRecType) {
	this.euRecType = euRecType;
}
public String getExpNote() {
	return expNote;
}
public void setExpNote(String expNote) {
	this.expNote = expNote;
}
public String getIsTurnToCp() {
	return isTurnToCp;
}
public void setIsTurnToCp(String isTurnToCp) {
	this.isTurnToCp = isTurnToCp;
}
public String getExpType() {
	return expType;
}
public void setExpType(String expType) {
	this.expType = expType;
}
public String getPhasePkCpexp() {
	return phasePkCpexp;
}
public void setPhasePkCpexp(String phasePkCpexp) {
	this.phasePkCpexp = phasePkCpexp;
}
public String getPhaseExpNote() {
	return phaseExpNote;
}
public void setPhaseExpNote(String phaseExpNote) {
	this.phaseExpNote = phaseExpNote;
}
public String getPkCpphase() {
	return pkCpphase;
}
public void setPkCpphase(String pkCpphase) {
	this.pkCpphase = pkCpphase;
}  

}
