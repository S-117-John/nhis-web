package com.zebone.nhis.pro.zsba.compay.ins.pub.vo;

import java.util.List;

public class InsPubSaveDetailedParam {

	private String jzjlh;
	private String insType;
	private String pkPi;
	private String pkPv;
	private String pkSettle;
	private List<InsPubDetailed> zsMx;
	public String getJzjlh() {
		return jzjlh;
	}
	public void setJzjlh(String jzjlh) {
		this.jzjlh = jzjlh;
	}
	public String getInsType() {
		return insType;
	}
	public void setInsType(String insType) {
		this.insType = insType;
	}
	public String getPkPi() {
		return pkPi;
	}
	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public String getPkSettle() {
		return pkSettle;
	}
	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}
	public List<InsPubDetailed> getZsMx() {
		return zsMx;
	}
	public void setZsMx(List<InsPubDetailed> zsMx) {
		this.zsMx = zsMx;
	}
}
