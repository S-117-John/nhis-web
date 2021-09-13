package com.zebone.nhis.cn.ipdw.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.cn.ipdw.CnSignCa;
import com.zebone.nhis.common.module.cn.ipdw.CpRecExpParam;

public class CnSignCaParam {
	List<String> pkCnList = new ArrayList<String>();
	List<CnSignCa> cnSignCaList = new ArrayList<CnSignCa>();
	List<CpRecExpParam> cpRecExp = new ArrayList<CpRecExpParam>();
	
	public List<String> getPkCnList() {
		return pkCnList;
	}
	public void setPkCnList(List<String> pkCnList) {
		this.pkCnList = pkCnList;
	}
	public List<CnSignCa> getCnSignCaList() {
		return cnSignCaList;
	}
	public void setCnSignCaList(List<CnSignCa> cnSignCaList) {
		this.cnSignCaList = cnSignCaList;
	}
	public List<CpRecExpParam> getCpRecExp() {
		return cpRecExp;
	}
	public void setCpRecExp(List<CpRecExpParam> cpRecExp) {
		this.cpRecExp = cpRecExp;
	}
	
}
