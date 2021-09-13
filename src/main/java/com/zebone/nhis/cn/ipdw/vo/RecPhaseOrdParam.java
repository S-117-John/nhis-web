package com.zebone.nhis.cn.ipdw.vo;

import com.zebone.nhis.cn.ipdw.vo.CnOrderVO;
import com.zebone.nhis.common.module.cn.ipdw.CnLabApply;
import com.zebone.nhis.common.module.cn.ipdw.CnOpApply;
import com.zebone.nhis.common.module.cn.ipdw.CnRisApply;
import com.zebone.nhis.common.module.cn.ipdw.CnSignCa;

public class RecPhaseOrdParam extends CnOrderVO{

	private String pkCpphase;
	private String nameCpphase;
	private String pkRecphase;
	private String pkCpord;
	private String pkCnord;
	private String pkTempwork;
	private String pkParent;
	private String nameCpord;
	private String pkRecdt;
	private String pkCpexp;
	private String expNote;
	private String pkCprec;
	private String expType;
	private String euType;
	private String btnType;
	private String flagNec;
	private CnRisApply risVo ;
	private CnLabApply lisVo ;
	private CnOpApply opVo;
	private CnSignCa cnSignCa;
	
	public String getPkCnord() {
		return pkCnord;
	}
	public void setPkCnord(String pkCnord) {
		this.pkCnord = pkCnord;
	}
	public String getPkTempwork() {
		return pkTempwork;
	}
	public void setPkTempwork(String pkTempwork) {
		this.pkTempwork = pkTempwork;
	}
	public String getEuType() {
		return euType;
	}
	public void setEuType(String euType) {
		this.euType = euType;
	}
	public String getExpType() {
		return expType;
	}
	public void setExpType(String expType) {
		this.expType = expType;
	}
	public String getPkCpphase() {
		return pkCpphase;
	}
	public void setPkCpphase(String pkCpphase) {
		this.pkCpphase = pkCpphase;
	}
	public String getNameCpphase() {
		return nameCpphase;
	}
	public void setNameCpphase(String nameCpphase) {
		this.nameCpphase = nameCpphase;
	}
	public String getPkRecphase() {
		return pkRecphase;
	}
	public void setPkRecphase(String pkRecphase) {
		this.pkRecphase = pkRecphase;
	}
	public String getPkCpord() {
		return pkCpord;
	}
	public void setPkCpord(String pkCpord) {
		this.pkCpord = pkCpord;
	}
	public String getNameCpord() {
		return nameCpord;
	}
	public void setNameCpord(String nameCpord) {
		this.nameCpord = nameCpord;
	}
	public String getPkRecdt() {
		return pkRecdt;
	}
	public void setPkRecdt(String pkRecdt) {
		this.pkRecdt = pkRecdt;
	}
	public String getPkCpexp() {
		return pkCpexp;
	}
	public void setPkCpexp(String pkCpexp) {
		this.pkCpexp = pkCpexp;
	}
	public String getExpNote() {
		return expNote;
	}
	public void setExpNote(String expNote) {
		this.expNote = expNote;
	}
	public String getPkCprec() {
		return pkCprec;
	}
	public void setPkCprec(String pkCprec) {
		this.pkCprec = pkCprec;
	}
	public CnRisApply getRisVo() {
		return risVo;
	}
	public void setRisVo(CnRisApply risVo) {
		this.risVo = risVo;
	}
	public CnLabApply getLisVo() {
		return lisVo;
	}
	public void setLisVo(CnLabApply lisVo) {
		this.lisVo = lisVo;
	}
	public CnOpApply getOpVo() {
		return opVo;
	}
	public void setOpVo(CnOpApply opVo) {
		this.opVo = opVo;
	}
	public String getPkParent() {
		return pkParent;
	}
	public void setPkParent(String pkParent) {
		this.pkParent = pkParent;
	}
	public String getBtnType() {
		return btnType;
	}
	public void setBtnType(String btnType) {
		this.btnType = btnType;
	}
	public String getFlagNec() {
		return flagNec;
	}
	public void setFlagNec(String flagNec) {
		this.flagNec = flagNec;
	}
	public CnSignCa getCnSignCa() {
		return cnSignCa;
	}
	public void setCnSignCa(CnSignCa cnSignCa) {
		this.cnSignCa = cnSignCa;
	}
	
}
