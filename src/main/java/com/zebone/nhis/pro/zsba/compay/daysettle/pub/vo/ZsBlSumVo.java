package com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo;

import java.util.Date;
import java.util.List;

public class ZsBlSumVo {
	
	private List<ZsBlSumDepoVo> depos;
	
	private List<ZsBlSumPayVo> payos;
	
	private ZsBlSumRecords sumInfo;
	
	private List<ZsPayerData> payerList;
	
	private String pkEmp;
	
	private String nameEmp;
	
	private String dateLeader;

	private List<ZsBaBlSumVo> blSumVoList;
	
	public ZsBlSumRecords getSumInfo() {
		return sumInfo;
	}

	public void setSumInfo(ZsBlSumRecords sumInfo) {
		this.sumInfo = sumInfo;
	}

	public List<ZsBlSumDepoVo> getDepos() {
		return depos;
	}

	public void setDepos(List<ZsBlSumDepoVo> depos) {
		this.depos = depos;
	}

	public List<ZsBlSumPayVo> getPayos() {
		return payos;
	}

	public void setPayos(List<ZsBlSumPayVo> payos) {
		this.payos = payos;
	}

	public List<ZsPayerData> getPayerList() {
		return payerList;
	}

	public void setPayerList(List<ZsPayerData> payerList) {
		this.payerList = payerList;
	}

	public String getPkEmp() {
		return pkEmp;
	}

	public void setPkEmp(String pkEmp) {
		this.pkEmp = pkEmp;
	}

	public String getNameEmp() {
		return nameEmp;
	}

	public void setNameEmp(String nameEmp) {
		this.nameEmp = nameEmp;
	}

	public String getDateLeader() {
		return dateLeader;
	}

	public void setDateLeader(String dateLeader) {
		this.dateLeader = dateLeader;
	}

	public List<ZsBaBlSumVo> getBlSumVoList() {
		return blSumVoList;
	}

	public void setBlSumVoList(List<ZsBaBlSumVo> blSumVoList) {
		this.blSumVoList = blSumVoList;
	}

}
