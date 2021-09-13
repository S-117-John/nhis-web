package com.zebone.nhis.common.module.compay.ins.qgyb;

import java.math.BigDecimal;
import java.util.List;

public class MzHpHuaJiaParam {
	//就诊主键
    public String PkPv;
    public String Jzbh ; 
    // 总费用
    // </summary>
    public BigDecimal TotalCost ;
    // 收费明细
    //public List<YBChargeDetail> ChargeDetails :
    // 待结项目主键
    public List<String> PkCgips;
    // 医保患者信息
    public Object YBPatientInfo ;    /// 医保挂号信息
    public Object YBRegInfo ;
	public String getPkPv() {
		return PkPv;
	}
	public void setPkPv(String pkPv) {
		PkPv = pkPv;
	}
	public String getJzbh() {
		return Jzbh;
	}
	public void setJzbh(String jzbh) {
		Jzbh = jzbh;
	}
	public BigDecimal getTotalCost() {
		return TotalCost;
	}
	public void setTotalCost(BigDecimal totalCost) {
		TotalCost = totalCost;
	}
	public List<String> getPkCgips() {
		return PkCgips;
	}
	public void setPkCgips(List<String> pkCgips) {
		PkCgips = pkCgips;
	}
	public Object getYBPatientInfo() {
		return YBPatientInfo;
	}
	public void setYBPatientInfo(Object yBPatientInfo) {
		YBPatientInfo = yBPatientInfo;
	}
	public Object getYBRegInfo() {
		return YBRegInfo;
	}
	public void setYBRegInfo(Object yBRegInfo) {
		YBRegInfo = yBRegInfo;
	}
    
    
}
