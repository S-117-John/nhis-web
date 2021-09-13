package com.zebone.nhis.common.module.cn.cp;

import java.util.Date;

import com.zebone.nhis.cn.ipdw.vo.CnIpPressVO;
import com.zebone.nhis.common.module.cn.ipdw.CnOpApply;
import com.zebone.nhis.common.module.cn.ipdw.CnOrdAnti;
import com.zebone.nhis.common.module.cn.ipdw.CnOrdAntiApply;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;

/**
 * 路径医嘱
 * @author Administrator
 *
 */

public class CpRecOrd extends CnOrder {
	
	private String pkCpphase;
	private String nameNhase;
	private String pkRecphase;
	private String pkRecdt;
	private String euStatus;
	private String note;
	private Date dateEx;
	private String pkEmpEx;
	private String nameEmpEx;
	private String pkCpord;
	private Integer sortno;
	private String pkParent;
	private String flagNec;
	private String euTasktype;
	private String euCpordtype;
	private String pkCptask;
	private String euReptype;
	private String dimNameOrd;
	private String dtPharm;
	private CpRecExp cpRecExp;
	private CnOpApply cnOpApply;
	private CnOrdAntiApply specAntiVo;
	private CnOrdAnti cnOrdAnti;
	private CnIpPressVO cnIpPres;
	
	
	
	
	public CnIpPressVO getCnIpPres() {
		return cnIpPres;
	}
	public void setCnIpPres(CnIpPressVO cnIpPres) {
		this.cnIpPres = cnIpPres;
	}
	public String getPkRecphase() {
		return pkRecphase;
	}
	public void setPkRecphase(String pkRecphase) {
		this.pkRecphase = pkRecphase;
	}
	public CnOrdAnti getCnOrdAnti() {
		return cnOrdAnti;
	}
	public void setCnOrdAnti(CnOrdAnti cnOrdAnti) {
		this.cnOrdAnti = cnOrdAnti;
	}
	public CnOrdAntiApply getSpecAntiVo() {
		return specAntiVo;
	}
	public void setSpecAntiVo(CnOrdAntiApply specAntiVo) {
		this.specAntiVo = specAntiVo;
	}
	public CnOpApply getCnOpApply() {
		return cnOpApply;
	}
	public void setCnOpApply(CnOpApply cnOpApply) {
		this.cnOpApply = cnOpApply;
	}
	public CpRecExp getCpRecExp() {
		return cpRecExp;
	}
	public void setCpRecExp(CpRecExp cpRecExp) {
		this.cpRecExp = cpRecExp;
	}
	public String getPkCpphase() {
		return pkCpphase;
	}
	public void setPkCpphase(String pkCpphase) {
		this.pkCpphase = pkCpphase;
	}
	public String getNameNhase() {
		return nameNhase;
	}
	public void setNameNhase(String nameNhase) {
		this.nameNhase = nameNhase;
	}
	public String getPkRecdt() {
		return pkRecdt;
	}
	public void setPkRecdt(String pkRecdt) {
		this.pkRecdt = pkRecdt;
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
	public Date getDateEx() {
		return dateEx;
	}
	public void setDateEx(Date dateEx) {
		this.dateEx = dateEx;
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
	public String getPkCpord() {
		return pkCpord;
	}
	public void setPkCpord(String pkCpord) {
		this.pkCpord = pkCpord;
	}
	public Integer getSortno() {
		return sortno;
	}
	public void setSortno(Integer sortno) {
		this.sortno = sortno;
	}
	public String getPkParent() {
		return pkParent;
	}
	public void setPkParent(String pkParent) {
		this.pkParent = pkParent;
	}
	public String getFlagNec() {
		return flagNec;
	}
	public void setFlagNec(String flagNec) {
		this.flagNec = flagNec;
	}
	public String getEuTasktype() {
		return euTasktype;
	}
	public void setEuTasktype(String euTasktype) {
		this.euTasktype = euTasktype;
	}
	public String getEuCpordtype() {
		return euCpordtype;
	}
	public void setEuCpordtype(String euCpordtype) {
		this.euCpordtype = euCpordtype;
	}
	public String getPkCptask() {
		return pkCptask;
	}
	public void setPkCptask(String pkCptask) {
		this.pkCptask = pkCptask;
	}
	public String getEuReptype() {
		return euReptype;
	}
	public void setEuReptype(String euReptype) {
		this.euReptype = euReptype;
	}
	public String getDimNameOrd() {
		return dimNameOrd;
	}
	public void setDimNameOrd(String dimNameOrd) {
		this.dimNameOrd = dimNameOrd;
	}
	public String getDtPharm() {
		return dtPharm;
	}
	public void setDtPharm(String dtPharm) {
		this.dtPharm = dtPharm;
	}
	
	
	

}
