package com.zebone.nhis.ex.nis.emr.vo;

import java.util.List;

import com.zebone.nhis.common.module.ex.nis.emr.ExVtsOcc;
import com.zebone.nhis.common.module.ex.nis.emr.ExVtsOccDt;

@SuppressWarnings("serial")
public class ExVtsOccByPv extends ExVtsOcc{
	private String codeIp;//住院号
	private String bedNo;//床号
	private String namePi;//姓名
	private String dtSex;//性别
	private String agePv;//年龄
	private String pkPvAs;//pv表的pk_pv,以这个字段为准
	private String pkPiAs;//pv表的pk_pi,以这个字段为准
	private List<ExVtsOccDt> listDts ;//体温观测数据
	
	public String getPkPvAs() {
		return pkPvAs;
	}

	public void setPkPvAs(String pkPvAs) {
		this.pkPvAs = pkPvAs;
	}

	public String getPkPiAs() {
		return pkPiAs;
	}

	public void setPkPiAs(String pkPiAs) {
		this.pkPiAs = pkPiAs;
	}
	
	public String getCodeIp() {
		return codeIp;
	}

	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}

	public List<ExVtsOccDt> getListDts() {
		return listDts;
	}

	public void setListDts(List<ExVtsOccDt> listDts) {
		this.listDts = listDts;
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

	public String getDtSex() {
		return dtSex;
	}

	public void setDtSex(String dtSex) {
		this.dtSex = dtSex;
	}

	public String getAgePv() {
		return agePv;
	}

	public void setAgePv(String agePv) {
		this.agePv = agePv;
	}

}
