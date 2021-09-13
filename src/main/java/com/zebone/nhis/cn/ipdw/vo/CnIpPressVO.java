package com.zebone.nhis.cn.ipdw.vo;

import java.util.List;

import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.CnPrescription;
import com.zebone.nhis.common.module.cn.ipdw.CnSignCa;



public class CnIpPressVO extends CnPrescription{
	private List<CnOrderVO> ords;
	private List<CnOrder> ordsForDel;
    private String pkCprec;
    private String pkCpexp;
    private String expNote;
    private String euStatusOrd;
    private String diagname;
    private String codeEmp;
    private List<CnOrder> cpExpOrdList;
    private String pkDeptEx; //执行科室
    private CnSignCa cnSignCa;//CA认证信息
	private String flagSettle;//处方缴费标志
	private int ordsCount;
	private double totalPrice;
	private String herbCnord;
	private String ifry;//是否人医客户化功能

	@Override
	public String getFlagSettle() {
		return flagSettle;
	}

	@Override
	public void setFlagSettle(String flagSettle) {
		this.flagSettle = flagSettle;
	}

	public CnSignCa getCnSignCa() {
		return cnSignCa;
	}

	public void setCnSignCa(CnSignCa cnSignCa) {
		this.cnSignCa = cnSignCa;
	}

	public String getPkDeptEx() {
		return pkDeptEx;
	}

	public void setPkDeptEx(String pkDeptEx) {
		this.pkDeptEx = pkDeptEx;
	}

	public List<CnOrder> getCpExpOrdList() {
		return cpExpOrdList;
	}

	public void setCpExpOrdList(List<CnOrder> cpExpOrdList) {
		this.cpExpOrdList = cpExpOrdList;
	}

	public String getCodeEmp() {
		return codeEmp;
	}

	public void setCodeEmp(String codeEmp) {
		this.codeEmp = codeEmp;
	}

	public String getDiagname() {
		return diagname;
	}

	public void setDiagname(String diagname) {
		this.diagname = diagname;
	}

	public List<CnOrder> getOrdsForDel() {
		return ordsForDel;
	}

	public void setOrdsForDel(List<CnOrder> ordsForDel) {
		this.ordsForDel = ordsForDel;
	}

	public String getEuStatusOrd() {
		return euStatusOrd;
	}

	public void setEuStatusOrd(String euStatusOrd) {
		this.euStatusOrd = euStatusOrd;
	}

	public List<CnOrderVO> getOrds() {
		return ords;
	}

	public void setOrds(List<CnOrderVO> ords) {
		this.ords = ords;
	}

	public String getPkCprec() {
		return pkCprec;
	}

	public void setPkCprec(String pkCprec) {
		this.pkCprec = pkCprec;
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

	public int getOrdsCount() {
		return ordsCount;
	}

	public void setOrdsCount(int ordsCount) {
		this.ordsCount = ordsCount;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getHerbCnord() {
		return herbCnord;
	}

	public void setHerbCnord(String herbCnord) {
		this.herbCnord = herbCnord;
	}

	public String getIfry() {
		return ifry;
	}

	public void setIfry(String ifry) {
		this.ifry = ifry;
	}
}
