package com.zebone.nhis.cn.opdw.vo;

import com.zebone.nhis.common.module.cn.ipdw.CnOrdHerb;

public class HerbPresDt extends CnOrdHerb{

     private  String name; //药品名称
     private  String unit ; //单位名称
     private  String flagAcc; //账户支付标志
     private  String flagSettle; //结算标志
     private String pkOrg;
     private String statusData;
     private String tstr;
     private Double stockQuan;//库存
     private  String nameOrd;
     private String flagStopPd;
     private String flagStopStore;
     private Double quanMin;
     
 	public Double getStockQuan() {
 		return stockQuan;
 	}
 	public void setStockQuan(Double stockQuan) {
 		this.stockQuan = stockQuan;
 	}
     
	public String getTstr() {
		return tstr;
	}
	public void setTstr(String tstr) {
		this.tstr = tstr;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getFlagAcc() {
		return flagAcc;
	}
	public void setFlagAcc(String flagAcc) {
		this.flagAcc = flagAcc;
	}
	public String getFlagSettle() {
		return flagSettle;
	}
	public void setFlagSettle(String flagSettle) {
		this.flagSettle = flagSettle;
	}
	public String getPkOrg() {
		return pkOrg;
	}
	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}
	public String getStatusData() {
		return statusData;
	}
	public void setStatusData(String statusData) {
		this.statusData = statusData;
	}
	public String getNameOrd() {
		return nameOrd;
	}
	public void setNameOrd(String nameOrd) {
		this.nameOrd = nameOrd;
	}
	public String getFlagStopPd() {
		return flagStopPd;
	}
	public void setFlagStopPd(String flagStopPd) {
		this.flagStopPd = flagStopPd;
	}
	public String getFlagStopStore() {
		return flagStopStore;
	}
	public void setFlagStopStore(String flagStopStore) {
		this.flagStopStore = flagStopStore;
	}
	public Double getQuanMin() {
		return quanMin;
	}
	public void setQuanMin(Double quanMin) {
		this.quanMin = quanMin;
	}
}
